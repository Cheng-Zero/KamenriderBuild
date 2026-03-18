package cheng.build.rider_syteam;

import cheng.build.ArmorUseHandler;
import cheng.build.Build;
import cheng.build.DelayedTask;
import cheng.build.api.IFullBottle;
import cheng.build.data.ABaseData;
import cheng.build.entity.BuildUpEffectEntity;
import cheng.build.entity.EffectEntity;
import cheng.build.entity.InorganicEntity;
import cheng.build.entity.OrganicEntity;
import cheng.build.init.InitItem;
import cheng.build.item.armor.BuildDriver;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class HenshinUtil extends ABaseData {
    ItemStack inorganic,organic;
    List<IFullBottle> fullBottles = BottleRegistry.getAllBottles();

    protected UUID OrganicEntitY,InorganicEntitY;

    public void summonEffectEntity(boolean isHenshinBefore) {
        inorganic = loadItem(driverTag, BuildDriver.inorganicMatter_item_Name);
        organic = loadItem(driverTag, BuildDriver.organicMatter_item_Name);

        IFullBottle Inorganic = BottleRegistry.findByItemStack(inorganic);
        IFullBottle Organic = BottleRegistry.findByItemStack(organic);

        if (isHenshinBefore) {
            if (isTheFullBottle(Inorganic,inorganic)){
                InorganicEntity(Inorganic);
            }
            if (isTheFullBottle(Organic,organic)){
                OrganicEntity(Organic);
            }
        }else {
            if (CurrentFormNotBottle(Inorganic,inorganic,chestplate)){
                InorganicEntity(Inorganic);
            }
            if (CurrentFormNotBottle(Organic,organic,helmet)){
                OrganicEntity(Organic);
            }
        }
    }


    private boolean isTheFullBottle(IFullBottle iFullBottle,ItemStack itemStack){
        return iFullBottle.getFullBottle().equals(itemStack.getItem());
    }

    private boolean CurrentFormNotBottle(IFullBottle iFullBottle, ItemStack itemStack, Item item){
        return !iFullBottle.getFullBottle().equals(itemStack.getItem()) && iFullBottle.getBuildArmor() == item;
    }

    private void InorganicEntity(IFullBottle bottle){
        InorganicEntity inorganicEntity = new InorganicEntity(player.level, BestMatchRegistry.isBestMatch(player), bottle.getBuildArmor().getTexture());
        Build.LOGGER.debug("生成实体{}",inorganicEntity);
        InorganicEntitY = DelayedTask.chain(player.level)
                .then(45, () -> InorganicEntity.addEntity(inorganicEntity, player))
                .then(15, inorganicEntity::discard)
                .start();
    }

    private void OrganicEntity(IFullBottle bottle){
        OrganicEntity organicEntity = new OrganicEntity(player.level, BestMatchRegistry.isBestMatch(player), bottle.getBuildArmor().getTexture());
        Build.LOGGER.debug("生成实体{}",organicEntity);
        OrganicEntitY = DelayedTask.chain(player.level)
                .then(45, () -> OrganicEntity.addEntity(organicEntity, player))
                .then(15, organicEntity::discard)
                .start();
    }

    public void HenshinBeforeEquieBottleArmor(boolean isHenshinBefore){
        inorganic = loadItem(driverTag, BuildDriver.inorganicMatter_item_Name);
        organic = loadItem(driverTag, BuildDriver.organicMatter_item_Name);
        boolean onFullBottle = organic.isEmpty() && inorganic.isEmpty();

        IFullBottle Inorganic = BottleRegistry.findByItemStack(inorganic);
        IFullBottle Organic = BottleRegistry.findByItemStack(organic);

        if (isHenshinBefore) {
            // 根据 注册内容判断
            if (player instanceof ServerPlayer serverPlayer)
                ArmorUseHandler.saveArmor(serverPlayer);
            setFullBottleArmor(onFullBottle);
            if (!onFullBottle) {
                player.setItemSlot(EquipmentSlot.FEET, new ItemStack(InitItem.buildBaseArmor.get()));
            }
        }else {
            fullBottles.forEach((d)-> {
                if (isHenshining(d, inorganic) && !chestplate.equals(d.getFullBottle()))
                    player.setItemSlot(EquipmentSlot.CHEST, new ItemStack(d.getBuildArmor()));
                if (isHenshining(d, organic) && !helmet.equals(d.getFullBottle()))
                    player.setItemSlot(EquipmentSlot.HEAD, new ItemStack(d.getBuildArmor()));
            });
        }
    }
    public boolean isHenshining(IFullBottle iFullBottle , ItemStack itemStack){
        inorganic = loadItem(driverTag, BuildDriver.inorganicMatter_item_Name);
        organic = loadItem(driverTag, BuildDriver.organicMatter_item_Name);
        boolean onFullBottle = organic.isEmpty() && inorganic.isEmpty();
        return iFullBottle.getFullBottle().equals(itemStack.getItem()) && !onFullBottle && isHenshin();
    }
    private void setFullBottleArmor(boolean onFullBottle){
        fullBottles.forEach((d)-> {
                    if (d.getFullBottle().equals(inorganic.getItem()) && !onFullBottle){
                        player.setItemSlot(EquipmentSlot.CHEST, new ItemStack(d.getBuildArmor()));
                    }
                    if (d.getFullBottle().equals(organic.getItem()) && !onFullBottle) {
                        player.setItemSlot(EquipmentSlot.HEAD, new ItemStack(d.getBuildArmor()));
                    }
                });
    }
    public void Summon(String Animation_build_up,String Animation_build_up_close,String Animation_build_up_over){
        if (effect() instanceof BuildUpEffectEntity)
            DelayedTask.chain(player.level)
                    .then(45, () -> {
                        effect().setAnimation(Animation_build_up);
                    })
                    .then(10, () -> {
                        effect().setAnimation(Animation_build_up_close);
                        HenshinBeforeEquieBottleArmor(true);
                    })
                    .then(30, () -> effect().setAnimation(Animation_build_up_over))
                    .then(32, () -> effect().setdiscard(player))
                    .then(1,()-> BestMatchRegistry.playSound(player,inorganic,organic))
                    .start();
    }

    protected BuildUpEffectEntity effect(){
        for (EffectEntity effectEntity : getEntity()) {
            if (effectEntity instanceof BuildUpEffectEntity entity && effectEntity.getOwner() == player)
                return entity;
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
