package cheng.build.bottle;

import cheng.build.init.InitItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ABaseData {
    protected Player player;
    protected ItemStack mainStack, offStack,leggingsStack;
    protected Item main, off, leggings ,driver;
    protected CompoundTag driverTag;
    protected boolean equieDriver;

    // 更新数据
    public void update(Player player) {
        if (player != null) {
            this.player = player;
            this.mainStack = player.getItemBySlot(EquipmentSlot.MAINHAND);
            this.offStack = player.getItemBySlot(EquipmentSlot.OFFHAND);
            this.leggingsStack = player.getItemBySlot(EquipmentSlot.LEGS);
            this.main = player.getItemBySlot(EquipmentSlot.MAINHAND).getItem();
            this.off = player.getItemBySlot(EquipmentSlot.OFFHAND).getItem();
            this.leggings = player.getItemBySlot(EquipmentSlot.LEGS).getItem();
            this.driver = InitItem.buildDriver.get();
            updateTag();
            this.equieDriver = leggings.equals(driver);
        }else{
            this.mainStack = ItemStack.EMPTY;
            this.offStack = ItemStack.EMPTY;
            this.leggingsStack = ItemStack.EMPTY;
            this.main = null;
            this.off = null;
            this.leggings = null;
            this.driver = null;
            this.equieDriver = false;
        }
    }

    protected enum ClientMessageEnum {
        Organic("key.kamenrider_build.clear_driver.organic_title"),
        Inorganic("key.kamenrider_build.clear_driver.inorganic_title"),
        Air("key.kamenrider_build.clear_driver.air");
        final String string;
        ClientMessageEnum(String string){
            this.string = string;
        }
    }
    private void updateTag(){
        this.driverTag = leggingsStack.getTag();
        if (driverTag == null)
            this.driverTag = leggingsStack.getOrCreateTag();
    }
    protected void ClientMessage(ClientMessageEnum key){
        this.ClientMessage(new TranslatableComponent(key.string),true);
    }
    protected void ClientMessage(Component key, boolean isAction){
        player.displayClientMessage(key,isAction);
    }
    // 存储物品并转换为NBT形式
    protected CompoundTag savePiece(ItemStack item) {
        CompoundTag tag = new CompoundTag();
        if (!item.isEmpty()) {
            item.save(tag);
        }
        return tag;
    }
}
