package cheng.build.program;

import cheng.build.Build;
import cheng.build.DelayedTask;
import cheng.build.ItemHelper;
import cheng.build.api.IFullBottle;
import cheng.build.data.DataManager;
import cheng.build.data.PlayerBuildData;
import cheng.build.data.ABaseData;
import cheng.build.item.armor.BuildDriver;
import cheng.build.item.bottle.bottle.Bottle;
import cheng.build.item.bottle.bottle.FullBottle;
import cheng.build.item.bottle.bottles.EmptyBottleItem;
import cheng.build.item.bottle.bottle.InorganicMatterBottleItem;
import cheng.build.item.bottle.bottle.OrganicMatterBottleItem;
import cheng.build.rider_syteam.BuildRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class BottleExecute extends ABaseData {
    String inorganic = BuildDriver.inorganicMatter_item_Name;
    String organic = BuildDriver.organicMatter_item_Name;
    /// 主程序
    public void OnDriver(ItemStack itemStack){
        PlayerBuildData data = DataManager.get(player);
        // 玩家不为null 非客户端执行
        boolean b = player != null && !player.level.isClientSide();
        if (!b) return;
        if (data.isEquieDriver()) {
            if (itemStack.getItem() instanceof Bottle){
                IfBottle(itemStack);
            }
        }
    }

    private void IfBottle(ItemStack itemStack) {
        Item item = itemStack.getItem();
        // 无机物满装瓶分支
        if (item instanceof InorganicMatterBottleItem) {
            InorganicMatterBottle(itemStack);
        }
        // 有机物满装瓶分支
        else if (item instanceof OrganicMatterBottleItem) {
            OrganicMatterBottle(itemStack);
        }
        // 空白瓶分支
        else if (item instanceof EmptyBottleItem) {
            emptybottle(itemStack);
        }
    }

    private void InorganicMatterBottle(ItemStack itemStack){
        Build.LOGGER.info("试图装载无机物满装瓶{}",itemStack.getItem());
        OnInorganicBottle(itemStack,inorganic);
    }
    private void OrganicMatterBottle(ItemStack itemStack){
        Build.LOGGER.info("试图装载有机物满装瓶{}",itemStack.getItem());
        OnOrganicBottle(itemStack,organic);
    }
    // 空白瓶
    private void emptybottle(ItemStack itemStack){
        PlayerBuildData data = DataManager.get(player);
        if (player.getUseItem() == player.getItemBySlot(EquipmentSlot.OFFHAND))
            OnOrganicBottle(itemStack,organic);
        else if (player.getUseItem() == player.getItemBySlot(EquipmentSlot.MAINHAND))
            OnInorganicBottle (itemStack,inorganic);

        run(40,()-> data.ClientMessage(new TranslatableComponent("key.kamenrider_build.clear_driver.what_do_you_mean"), true));
    }
    private void OnOrganicBottle(ItemStack itemStack,String tagName){
        PlayerBuildData data = DataManager.get(player);
        CompoundTag driverTag = data.getDriverTag();
        if (driverTag.getCompound(tagName).isEmpty())
            OnBottle(tagName, itemStack);
        else ClientOrganicMessage();
    }
    private void OnInorganicBottle(ItemStack itemStack,String tagName){
        PlayerBuildData data = DataManager.get(player);
        CompoundTag driverTag = data.getDriverTag();
        if (driverTag.getCompound(tagName).isEmpty())
            OnBottle(tagName, itemStack);
        else ClientInorganicMessage();
    }
    private void OnBottle(String pKey,ItemStack itemStack){
        PlayerBuildData data = new PlayerBuildData(player);
        swingHand();
        playSound(itemStack);
        player.getItemBySlot(EquipmentSlot.LEGS).getOrCreateTag().put(pKey,data.savePiece(itemStack));
        ItemHelper.removeItem(player, itemStack);
    }

    protected void playSound(ItemStack itemStack){
        PlayerBuildData data = DataManager.get(player);
        CompoundTag driverTag = data.getDriverTag();
        Item mainHand = player.getItemBySlot(EquipmentSlot.MAINHAND).getItem();
        Item offHand = player.getItemBySlot(EquipmentSlot.OFFHAND).getItem();
        if (itemStack.getItem() instanceof FullBottle fullBottle) {
            // 如果主手拿着 无机物满装瓶 的同时副手拿着 有机物满装瓶
            if (mainHand instanceof InorganicMatterBottleItem inorganicMatterBottleItem && offHand instanceof OrganicMatterBottleItem organicMatterBottleItem) {
                doubleBottle(itemStack, inorganicMatterBottleItem, organicMatterBottleItem);
            }
            // 如果副手拿着 无机物满装瓶 的同时主手拿着 有机物满装瓶
            else if (offHand instanceof InorganicMatterBottleItem inorganicMatterBottleItem && mainHand instanceof OrganicMatterBottleItem organicMatterBottleItem) {
                doubleBottle(itemStack, inorganicMatterBottleItem, organicMatterBottleItem);
            }
            // 否则正常播放音效
            else {
                playSound(fullBottle);
                // 都不为空触发BestMatch
                if (!(driverTag.getCompound(organic).isEmpty() && driverTag.getCompound(inorganic).isEmpty()))
                    bestBatch();
            }
        }
    }
    // 没错是穷举法（其实，我并不知道为什么是这样，但他就是这样的）
    private void doubleBottle(ItemStack itemStack, InorganicMatterBottleItem inorganicMatterBottleItem, OrganicMatterBottleItem organicMatterBottleItem){
        PlayerBuildData data = DataManager.get(player);
        CompoundTag driverTag = data.getDriverTag();
        UUID playSoundInorganic;
        UUID playSoundOrganic;
        UUID playSoundBestMatch;
        playSoundInorganic = run(20, ()-> playSound(inorganicMatterBottleItem));
        playSoundOrganic = run(1, ()-> playSound(organicMatterBottleItem));
        playSoundBestMatch = run(20, this::bestBatch);
        // 物品 为无机物满装瓶 以及 腰带为无
        if (itemStack.getItem() == inorganicMatterBottleItem && driverTag.getCompound(inorganic).isEmpty()){
            Build.LOGGER.info("播放{}无机物插入音效", inorganicMatterBottleItem.getRegistryName());
            DelayedTask.cancel(playSoundOrganic);
        }else
        if (itemStack.getItem() == organicMatterBottleItem && driverTag.getCompound(organic).isEmpty()){
            Build.LOGGER.info("播放{}有机物插入音效", inorganicMatterBottleItem.getRegistryName());
            DelayedTask.cancel(playSoundInorganic);
        }
        if (driverTag.getCompound(inorganic).isEmpty() && driverTag.getCompound(organic).isEmpty()){
            Build.LOGGER.info("播放最佳搭配音效");
            DelayedTask.cancel(playSoundBestMatch);
        }
    }
    private void playSound(FullBottle fullBottle){
        IFullBottle byItem = BuildRegistry.FullBottlefindByItem(fullBottle);
        fullBottle.playsound(byItem,player);
    }
    private void bestBatch() {
        DelayedTask.run(player.level, 20, () -> {
            // 是否触发BestMatch
            if (BuildRegistry.isBestMatch(player))
                BuildRegistry.playBestMatchSound(player);
        });
    }


    private void ClientOrganicMessage(){
        PlayerBuildData data = DataManager.get(player);
        data.ClientMessage(PlayerBuildData.ClientMessageEnum.Organic);
    }
    private void ClientInorganicMessage(){
        PlayerBuildData data = DataManager.get(player);
        data.ClientMessage(PlayerBuildData.ClientMessageEnum.Inorganic);
    }

    // 摇动手臂
    private void swingHand() {
        ItemStack mainHandStack = player.getItemBySlot(EquipmentSlot.MAINHAND);
        ItemStack offHandStack = player.getItemBySlot(EquipmentSlot.OFFHAND);
        if (player.getUseItem() == mainHandStack) {
            swingHand(InteractionHand.OFF_HAND);
        } else if (player.getUseItem() == offHandStack) {
            swingHand(InteractionHand.MAIN_HAND);
        } else {
            swingHand(InteractionHand.OFF_HAND);
            DelayedTask.run(player.level, 5, () -> swingHand(InteractionHand.MAIN_HAND));
        }
    }
    private void swingHand(InteractionHand hand){
        player.swing(hand,true);
    }
    private UUID run(int delayTicks,Runnable runnable){
        return DelayedTask.run(player.level,delayTicks,runnable);
    }
}
