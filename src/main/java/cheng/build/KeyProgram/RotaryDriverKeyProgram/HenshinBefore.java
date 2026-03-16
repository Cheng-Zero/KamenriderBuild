package cheng.build.KeyProgram.RotaryDriverKeyProgram;

import cheng.build.ArmorUseHandler;
import cheng.build.Build;
import cheng.build.DelayedTask;
import cheng.build.item.armor.BuildDriver;
import cheng.build.data.BottleArmor;
import cheng.build.entity.BuildUpEffectEntity;
import cheng.build.entity.EffectEntity;
import cheng.build.entity.InorganicEntity;
import cheng.build.entity.OrganicEntity;
import cheng.build.data.BestMatch;
import cheng.build.init.InitItem;
import cheng.build.player_animation.PlayerAnimationUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class HenshinBefore extends RotaryDriver {
    protected void Henshin(){
        boolean empty =
                driverTag.getCompound(BuildDriver.organicMatter_item_Name).isEmpty() ||
                driverTag.getCompound(BuildDriver.inorganicMatter_item_Name).isEmpty();
        if (hasBuildUpEntity() || empty) return;

        if (player instanceof ServerPlayer serverPlayer)
            PlayerAnimationUtil.playanimation(serverPlayer, "ddd", true);
        driverTag.putBoolean("isUse", true);

        BuildUpEffectEntity.addEntity(effect(), player);

        effect().setAnimation(Animation_start);

        DelayedTask.chain(player.level)
                .then(40, () -> effect().setAnimation(Animation_standby_start))
                .then(1, () -> effect().setAnimation(Animation_standbying))
                .start();
    }
    protected void stop(){
        ItemStack inorganic = loadItem(driverTag, BuildDriver.inorganicMatter_item_Name);
        ItemStack organic = loadItem(driverTag, BuildDriver.organicMatter_item_Name);
        boolean onFullBottle = organic.isEmpty() && inorganic.isEmpty();
        if (!onFullBottle && driverTag.getBoolean("isUse")) {
            Build.LOGGER.info("按键抬起成功");
            if (player instanceof ServerPlayer serverPlayer)
                PlayerAnimationUtil.playanimation(serverPlayer, "ddd", false);
            summon();
            if (effect() instanceof BuildUpEffectEntity)
                DelayedTask.chain(player.level)
                        .then(45, () -> {
                            effect().setAnimation(Animation_build_up);
                        })
                        .then(10, () -> {
                            effect().setAnimation(Animation_build_up_close);
                            EquieBottleArmor();
                        })
                        .then(30, () -> effect().setAnimation(Animation_build_up_over))
                        .then(32, () -> effect().setdiscard(player))
                        .start();

            driverTag.putBoolean("isUse", false);
        }
    }

    private void summon(){
        ItemStack inorganic = loadItem(driverTag, BuildDriver.inorganicMatter_item_Name);
        ItemStack organic = loadItem(driverTag, BuildDriver.organicMatter_item_Name);
        for (BottleArmor bottleArmor :BottleArmor.bottleArmorList()){
            if (bottleArmor.bottle().equals(inorganic.getItem())) {
                InorganicEntity inorganicEntity = new InorganicEntity(player.level, BestMatch.isBestMatch(player), bottleArmor.texture());
                DelayedTask.chain(player.level)
                        .then(45, () -> InorganicEntity.addEntity(inorganicEntity,player)).then(15, inorganicEntity::discard)
                        .start();
            } else if (bottleArmor.bottle().equals(organic.getItem())) {
                OrganicEntity organicEntity = new OrganicEntity(player.level, BestMatch.isBestMatch(player), bottleArmor.texture());
                DelayedTask.chain(player.level)
                        .then(45, () -> OrganicEntity.addEntity(organicEntity,player))
                        .then(15, organicEntity::discard)
                        .start();
            }
        }
    }

    private void EquieBottleArmor(){
        ItemStack inorganic = loadItem(driverTag, BuildDriver.inorganicMatter_item_Name);
        ItemStack organic = loadItem(driverTag, BuildDriver.organicMatter_item_Name);
        boolean onFullBottle = organic.isEmpty() && inorganic.isEmpty();
        // 根据 枚举里的 枚举来判断和设置
        if (player instanceof ServerPlayer serverPlayer)
            ArmorUseHandler.saveArmor(serverPlayer);
        for (BottleArmor bottleArmor :BottleArmor.bottleArmorList()) {
            if (bottleArmor.bottle().equals(inorganic.getItem()) && !onFullBottle) {
                player.setItemSlot(EquipmentSlot.CHEST, new ItemStack(bottleArmor.armorItem()));
            }
            if (bottleArmor.bottle().equals(organic.getItem()) && !onFullBottle) {
                player.setItemSlot(EquipmentSlot.HEAD, new ItemStack(bottleArmor.armorItem()));
            }
            if (!onFullBottle) {
                player.setItemSlot(EquipmentSlot.FEET, new ItemStack(InitItem.buildBaseArmor.get()));
            }
        }
    }

    /// 有BuildUp实体就返回，不进行操作
    private boolean hasBuildUpEntity(){
        for (EffectEntity en : effect().getEntity(player))
            if (en.getOwner()==player)
                return true;
        return false;
    }
}
