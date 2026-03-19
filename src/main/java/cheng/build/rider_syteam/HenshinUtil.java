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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public class HenshinUtil extends ABaseData {
    ItemStack inorganic,organic;

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
            if (CurrentFormNotBottle(inorganic,chestplateStack)){
                InorganicEntity(Inorganic);
            }
            if (CurrentFormNotBottle(organic,helmetStack)){
                OrganicEntity(Organic);
            }
        }
    }


    private boolean isTheFullBottle(IFullBottle iFullBottle,ItemStack itemStack){
        return iFullBottle.getFullBottle().equals(itemStack.getItem());
    }

    public boolean CurrentFormNotBottle(ItemStack itemStack, ItemStack item){
        Build.LOGGER.debug("Inorganic Item：{}",itemStack);
        Build.LOGGER.debug("Organic Item：{}",item);
        IFullBottle byItemStack = BottleRegistry.findByItemStack(itemStack);
        IFullBottle byItem = BottleRegistry.findByArmorItem(item.getItem());
        Build.LOGGER.debug("IFullBottle Inorganic：{}",byItemStack);
        Build.LOGGER.debug("IFullBottle Organic：{}",byItem);
        // 如果瓶子不存在，或者瓶子与当前装备的护甲不匹配
        return byItemStack == null || byItem == null || !byItemStack.equals(byItem);
    }

    private void InorganicEntity(IFullBottle bottle){
        InorganicEntity inorganicEntity = new InorganicEntity(player.level, BestMatchRegistry.isBestMatch(player), bottle.getBuildArmor().getTexture(),player);
        Build.LOGGER.debug("生成实体{}",inorganicEntity);
        InorganicEntitY = DelayedTask.chain(player.level)
                .then(45, () -> InorganicEntity.addEntity(inorganicEntity, player))
                .then(15, inorganicEntity::discard)
                .start();
    }

    private void OrganicEntity(IFullBottle bottle){
        OrganicEntity organicEntity = new OrganicEntity(player.level, BestMatchRegistry.isBestMatch(player), bottle.getBuildArmor().getTexture(),player);
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

            EquieBottleArmor(Inorganic,Organic,onFullBottle);

        }else {
            EquieBottleArmor(Inorganic,Organic);
        }
    }
    public boolean isHenshining(IFullBottle iFullBottle , ItemStack itemStack){
        inorganic = loadItem(driverTag, BuildDriver.inorganicMatter_item_Name);
        organic = loadItem(driverTag, BuildDriver.organicMatter_item_Name);
        boolean onFullBottle = organic.isEmpty() && inorganic.isEmpty();
        if (iFullBottle.getFullBottle() == null) return false;
        return iFullBottle.getFullBottle().equals(itemStack.getItem()) && !onFullBottle && isHenshin();
    }

    /**
     * 变身后
     * 装备有机物 和 无机物 装甲
     * @param Inorganic 无机物
     * @param Organic 有机物
     */
    public void EquieBottleArmor(IFullBottle Inorganic,IFullBottle Organic){
        // 装备无机物
        if (isHenshining(Inorganic, inorganic) && !chestplate.equals(Inorganic.getFullBottle()))
            player.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Inorganic.getBuildArmor()));
        // 装备有机物
        if (isHenshining(Organic, organic) && !helmet.equals(Organic.getFullBottle()))
            player.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Organic.getBuildArmor()));
    }

    /**
     * 变身前
     * 装备有机物 和 无机物 和 基础 装甲
     * @param Inorganic 无机物
     * @param Organic 有机物
     */
    public void EquieBottleArmor(IFullBottle Inorganic,IFullBottle Organic,boolean onFullBottle){
        // 无机物
        if (Inorganic.getFullBottle().equals(inorganic.getItem()) && !onFullBottle)
            player.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Inorganic.getBuildArmor()));
        // 有机物
        if (Organic.getFullBottle().equals(organic.getItem()) && !onFullBottle)
            player.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Organic.getBuildArmor()));
        // 基础
        if (!onFullBottle)
            player.setItemSlot(EquipmentSlot.FEET, new ItemStack(InitItem.buildBaseArmor.get()));

    }

    public void Summon(String Animation_build_up,String Animation_build_up_close,String Animation_build_up_over){
        if (getEffectEntity() instanceof BuildUpEffectEntity) {
            BuildUpEffectEntity effectEntity = getEffectEntity();

            DelayedTask.chain(player.level)
                    .then(45, () -> {
                        effectEntity.setAnimation(Animation_build_up);
                    })
                    .then(10, () -> {
                        effectEntity.setAnimation(Animation_build_up_close);
                        HenshinBeforeEquieBottleArmor(true);
                    })
                    .then(30, () -> effectEntity.setAnimation(Animation_build_up_over))
                    .then(32, () -> effectEntity.setdiscard(player))
                    .then(1, () -> BestMatchRegistry.playSound(player, inorganic, organic))
                    .start();
        }
    }

    public BuildUpEffectEntity getEffectEntity(){
        for (EffectEntity effectEntity : getEntityList(EffectEntity.class,4/2d)) {
            if (effectEntity instanceof BuildUpEffectEntity entity && effectEntity.getOwner() == player)
                return entity;
        }
        return new BuildUpEffectEntity(player.level);
    }

    /**
     * 获取玩家周围的实体
     * @param EntityClass 实体类
     * @param value 半径
     * @return 实体集合
     * @param <T> 扩展于{@code Entity}
     */
    public <T extends Entity> List<T> getEntityList(Class<T> EntityClass,double value){
        final Vec3 playerXYZ = new Vec3(player.getX(), player.getY(), player.getZ());
        AABB aabb = new AABB(playerXYZ,playerXYZ).inflate(value);
        // 获取世界实体类型(Class)  目标实体.class    获取范围         条件判断具体目标
        return player.level.getEntitiesOfClass(EntityClass, aabb);
    }
}
