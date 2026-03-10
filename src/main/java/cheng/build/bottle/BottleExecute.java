package cheng.build.bottle;

import cheng.build.Build;
import cheng.build.DelayedTask;
import cheng.build.ItemHelper;
import cheng.build.SoundUtil;
import cheng.build.armor.BuildDriver;
import cheng.build.init.BestMatch;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class BottleExecute extends ABaseData {
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
    private void bestBatch(){
        // 是否触发BestMatch
        if (BestMatch.isBestMatch(player)){
            DelayedTask.run(player.level,20,()->BestMatch.playSound(player));
        }
    }

    private void InorganicMatterBottle(ItemStack itemStack){
        Build.LOGGER.info("试图装载无机物满装瓶{}",itemStack.getDescriptionId());
        if (GetTagisNoEmpty(BuildDriver.inorganicMatter_item_Name)) {
            OnBottle(BuildDriver.inorganicMatter_item_Name, itemStack);
            bestBatch();
        }
        else
            ClientInorganicMessage();
    }
    private void OrganicMatterBottle(ItemStack itemStack){
        Build.LOGGER.info("试图装载有机物满装瓶{}",itemStack.getDescriptionId());
        if (GetTagisNoEmpty(BuildDriver.organicMatter_item_Name)) {
            OnBottle("organicMatter_item", itemStack);
            bestBatch();
        }
        else
            ClientOrganicMessage();
    }
    /// 特殊类型 空白瓶
    private void emptybottle(ItemStack itemStack){
        if (player.getUseItem() == player.getItemBySlot(EquipmentSlot.OFFHAND)) {
            if (GetTagisNoEmpty(BuildDriver.organicMatter_item_Name)) {
                OnBottle("organicMatter_item", itemStack);
                bestBatch();
            }
            else
                ClientOrganicMessage();
        } else if (player.getUseItem() == player.getItemBySlot(EquipmentSlot.MAINHAND))
            if (GetTagisNoEmpty(BuildDriver.inorganicMatter_item_Name)) {
                OnBottle("inorganicMatter_item", itemStack);
                bestBatch();
            }
            else
                ClientInorganicMessage();
        DelayedTask.run(player.level,40,()-> ClientMessage(new TranslatableComponent("key.kamenrider_build.clear_driver.what_do_you_mean"), true));
    }
    private void OnBottle(String pKey,ItemStack itemStack){
        player.getItemBySlot(EquipmentSlot.LEGS).getOrCreateTag().put(pKey,savePiece(itemStack));
        swingHand();
        if (itemStack.getItem() instanceof FullBottle fullBottle)
            SoundUtil.playSound(player.level, (ServerPlayer) player, fullBottle.getSoundEvent().get(), SoundSource.PLAYERS);
        ItemHelper.removeItem(player, itemStack);
    }
    /// 基础判断：玩家不为null 非客户端执行
    private boolean baseBoolean(){
       return player != null && !player.level.isClientSide();
    }

    /// 获取腰带数据为空
    private boolean GetTagisNoEmpty(String tagName){
        return driverTag.getCompound(tagName).isEmpty();
    }

    private void ClientOrganicMessage(){
        ClientMessage(ClientMessage.Organic);
    }
    private void ClientInorganicMessage(){
        ClientMessage(ClientMessage.Inorganic);
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
}
