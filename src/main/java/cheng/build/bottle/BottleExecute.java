package cheng.build.bottle;

import cheng.build.Build;
import cheng.build.DelayedTask;
import cheng.build.ItemHelper;
import cheng.build.SoundUtil;
import cheng.build.armor.BuildDriver;
import cheng.build.bottle.bottles.Bottle;
import cheng.build.bottle.bottles.EmptyBottle;
import cheng.build.bottle.bottles.InorganicMatterBottle;
import cheng.build.bottle.bottles.OrganicMatterBottle;
import cheng.build.init.BestMatch;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class BottleExecute extends ABaseData {
    String inorganic = BuildDriver.inorganicMatter_item_Name;
    String organic = BuildDriver.organicMatter_item_Name;
    /// 主程序
    public void OnDriver(ItemStack itemStack){
        if (!baseBoolean()) return;
        if (equieDriver) {
            if (itemStack.getItem() instanceof Bottle){
                IfBottle(itemStack);
            }
        }
    }

    private void IfBottle(ItemStack itemStack) {
        // 无机物满装瓶分支
        if (itemStack.getItem() instanceof InorganicMatterBottle) {
            InorganicMatterBottle(itemStack);
        }
        // 有机物满装瓶分支
        else if (itemStack.getItem() instanceof OrganicMatterBottle) {
            OrganicMatterBottle(itemStack);
        }
        // 空白瓶分支
        else if (itemStack.getItem() instanceof EmptyBottle) {
            emptybottle(itemStack);
        }
    }

    private void InorganicMatterBottle(ItemStack itemStack){
        Build.LOGGER.info("试图装载无机物满装瓶{}",itemStack.getDescriptionId());
        OnInorganicBottle(itemStack,inorganic);
    }
    private void OrganicMatterBottle(ItemStack itemStack){
        Build.LOGGER.info("试图装载有机物满装瓶{}",itemStack.getDescriptionId());
        OnOrganicBottle(itemStack,organic);
    }
    // 空白瓶
    private void emptybottle(ItemStack itemStack){
        if (player.getUseItem() == player.getItemBySlot(EquipmentSlot.OFFHAND))
            OnOrganicBottle(itemStack,organic);
        else if (player.getUseItem() == player.getItemBySlot(EquipmentSlot.MAINHAND))
            OnInorganicBottle (itemStack,inorganic);

        run(40,()-> ClientMessage(new TranslatableComponent("key.kamenrider_build.clear_driver.what_do_you_mean"), true));
    }
    private void OnOrganicBottle(ItemStack itemStack,String tagName){
        if (driverTag.getCompound(tagName).isEmpty())
            OnBottle(tagName, itemStack);
        else ClientOrganicMessage();
    }
    private void OnInorganicBottle(ItemStack itemStack,String tagName){
        if (driverTag.getCompound(tagName).isEmpty())
            OnBottle(tagName, itemStack);
        else ClientInorganicMessage();
    }
    private void OnBottle(String pKey,ItemStack itemStack){
        swingHand();
        playSound(itemStack);
        player.getItemBySlot(EquipmentSlot.LEGS).getOrCreateTag().put(pKey,savePiece(itemStack));
        ItemHelper.removeItem(player, itemStack);
    }

    protected void playSound(ItemStack itemStack){
        if (itemStack.getItem() instanceof FullBottle fullBottle) {
            // 如果主手拿着 无机物满装瓶 的同时副手拿着 有机物满装瓶
            if (main instanceof InorganicMatterBottle inorganicMatterBottle && off instanceof OrganicMatterBottle organicMatterBottle) {
                doubleBottle(itemStack,inorganicMatterBottle,organicMatterBottle);
            }
            // 如果副手拿着 无机物满装瓶 的同时主手拿着 有机物满装瓶
            else if (off instanceof InorganicMatterBottle inorganicMatterBottle && main instanceof OrganicMatterBottle organicMatterBottle) {
                doubleBottle(itemStack,inorganicMatterBottle,organicMatterBottle);
            }
            // 否则正常播放音效
            else {
                SoundUtil.playSound(player.level, (ServerPlayer) player, fullBottle.getSoundEvent().get(), SoundSource.PLAYERS);
                bestBatch();
            }
        }
    }
    // 没错是穷举法（其实，我并不知道为什么是这样，但他就是这样的）
    private void doubleBottle(ItemStack itemStack,InorganicMatterBottle inorganicMatterBottle,OrganicMatterBottle organicMatterBottle){
        UUID playSoundInorganic;
        UUID playSoundOrganic;
        UUID playSoundBestMatch;
        playSoundInorganic = run(20, ()-> playSound(inorganicMatterBottle));
        playSoundOrganic = run(1, ()-> playSound(organicMatterBottle));
        playSoundBestMatch = run(20, this::bestBatch);
        // 物品 为无机物满装瓶 以及 腰带为无
        if (itemStack.getItem() == inorganicMatterBottle && driverTag.getCompound(inorganic).isEmpty()){
            Build.LOGGER.info("播放{}无机物插入音效",inorganicMatterBottle.getRegistryName());
            DelayedTask.cancel(playSoundOrganic);
        }else
        if (itemStack.getItem() == organicMatterBottle && driverTag.getCompound(organic).isEmpty()){
            Build.LOGGER.info("播放{}有机物插入音效",inorganicMatterBottle.getRegistryName());
            DelayedTask.cancel(playSoundInorganic);
        }
        if (driverTag.getCompound(inorganic).isEmpty() && driverTag.getCompound(organic).isEmpty()){
            Build.LOGGER.info("播放最佳搭配音效");
            DelayedTask.cancel(playSoundBestMatch);
        }
    }
    private void playSound(FullBottle fullBottle){
        SoundUtil.playSound(player.level, (ServerPlayer) player, fullBottle.getSoundEvent().get(), SoundSource.PLAYERS);
    }
    private void bestBatch() {
        DelayedTask.run(player.level, 20, () -> {
            // 是否触发BestMatch
            if (BestMatch.isBestMatch(player))
                BestMatch.playSound(player);
        });
    }

    /// 基础判断：玩家不为null 非客户端执行
    private boolean baseBoolean(){
       return player != null && !player.level.isClientSide();
    }

    private void ClientOrganicMessage(){
        ClientMessage(ClientMessageEnum.Organic);
    }
    private void ClientInorganicMessage(){
        ClientMessage(ClientMessageEnum.Inorganic);
    }

    // 摇动手臂
    private void swingHand() {
        if (player.getUseItem() == mainStack) {
            swingHand(InteractionHand.OFF_HAND);
        } else if (player.getUseItem() == offStack) {
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
