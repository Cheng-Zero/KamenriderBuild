package cheng.build.bottle;

import cheng.build.ArmorUseHandler;
import cheng.build.Build;
import cheng.build.DelayedTask;
import cheng.build.armor.BuildDriver;
import cheng.build.entity.EffectEntity;
import cheng.build.entity.InorganicEntity;
import cheng.build.entity.OrganicEntity;
import cheng.build.init.BestMatch;
import cheng.build.init.InitItem;
import cheng.build.player_animation.PlayerAnimationUtil;
import cheng.build.entity.BuildUpEffectEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public class VortexLever extends ABaseData{
    private static final int
            CurrunStartTime = 0,
            CurrunEndTime = 0;
    private static final String
            Animation_start = "start",
            Animation_standby_start = "standby_start",
            Animation_standbying = "standbying",
            Animation_build_up = "build_up",
            Animation_build_up_close = "build_up_close",
            Animation_build_up_over = "build_up_over";

    public void on() {
        if (equieDriver && hasBuildUpEntity()) {
            Build.LOGGER.info("使用成功");
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
    }
    public void off() {
        ItemStack inorganic = BuildDriver.loadItem(driverTag, BuildDriver.inorganicMatter_item_Name);
        ItemStack organic = BuildDriver.loadItem(driverTag, BuildDriver.organicMatter_item_Name);
        boolean onFullBottle = organic.isEmpty() && inorganic.isEmpty();
        if (equieDriver && !onFullBottle && driverTag.getBoolean("isUse")) {
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

    private void EquieBottleArmor(){
        ItemStack inorganic = BuildDriver.loadItem(driverTag, BuildDriver.inorganicMatter_item_Name);
        ItemStack organic = BuildDriver.loadItem(driverTag, BuildDriver.organicMatter_item_Name);
        boolean onFullBottle = organic.isEmpty() && inorganic.isEmpty();
        // 根据 枚举里的 枚举来判断和设置
        if (player instanceof ServerPlayer serverPlayer)
            ArmorUseHandler.saveArmor(serverPlayer);
        for (BottleArmor bottleArmor :BottleArmor.bottleArmorList()) {
            if (bottleArmor.bottle.equals(inorganic.getItem()) && !onFullBottle) {
                player.setItemSlot(EquipmentSlot.CHEST, new ItemStack(bottleArmor.armorItem));
            }
            if (bottleArmor.bottle.equals(organic.getItem()) && !onFullBottle) {
                player.setItemSlot(EquipmentSlot.HEAD, new ItemStack(bottleArmor.armorItem));
            }
            if (!onFullBottle) {
                player.setItemSlot(EquipmentSlot.FEET, new ItemStack(InitItem.buildBaseArmor.get()));
            }
        }
    }
    private void summon(){
        ItemStack inorganic = BuildDriver.loadItem(driverTag, BuildDriver.inorganicMatter_item_Name);
        ItemStack organic = BuildDriver.loadItem(driverTag, BuildDriver.organicMatter_item_Name);
        for (BottleArmor bottleArmor :BottleArmor.bottleArmorList()){
            if (bottleArmor.bottle.equals(inorganic.getItem())) {
                InorganicEntity inorganicEntity = new InorganicEntity(player.level, BestMatch.isBestMatch(player), bottleArmor.texture);
                DelayedTask.chain(player.level)
                        .then(45, () -> InorganicEntity.addEntity(inorganicEntity,player)).then(15, inorganicEntity::discard)
                        .start();
            } else if (bottleArmor.bottle.equals(organic.getItem())) {
                OrganicEntity organicEntity = new OrganicEntity(player.level, BestMatch.isBestMatch(player), bottleArmor.texture);
                DelayedTask.chain(player.level)
                        .then(45, () -> OrganicEntity.addEntity(organicEntity,player))
                        .then(15, organicEntity::discard)
                        .start();
            }
        }
    }

    private boolean hasBuildUpEntity(){
        for (EffectEntity en : effect().getEntity(player))
            if (en.getOwner()==player)
                return false;
        return true;
    }
    private BuildUpEffectEntity effect(){
        for (EffectEntity effectEntity : getEntity()) {
            if (effectEntity instanceof BuildUpEffectEntity entity && effectEntity.getOwner() == player)
                return entity;
            else return new BuildUpEffectEntity(player.level);
        }
        return new BuildUpEffectEntity(player.level);
    }
    private List<EffectEntity> getEntity(){
        final Vec3 playerXYZ = new Vec3(player.getX(), player.getY(), player.getZ());
        AABB aabb = new AABB(playerXYZ,playerXYZ).inflate(4/2d);
        // 获取世界实体类型(Class)  目标实体.class    获取范围         条件判断具体目标
        return player.level.getEntitiesOfClass(EffectEntity.class, aabb, e -> true)
                // 转换成 数据流
                .stream()
                // 距离排序（影响处理实体先后顺序）
                .sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(playerXYZ)))
                // 转换为List列表
                .toList();
    }
}
