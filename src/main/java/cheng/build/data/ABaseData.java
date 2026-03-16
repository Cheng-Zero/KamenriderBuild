package cheng.build.data;

import cheng.build.item.armor.base.BuildArmor;
import cheng.build.init.InitItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ABaseData {
    protected Player player;
    protected ItemStack mainStack, offStack, helmetStack, chestplateStack, leggingsStack, bootsStack;
    protected Item main, off, helmet, chestplate, leggings, boots, driver;
    protected CompoundTag driverTag;
    protected boolean equieDriver;

    // 更新数据
    public void update(Player player) {
        if (player != null) {
            this.player = player;
            this.mainStack = player.getItemBySlot(EquipmentSlot.MAINHAND);
            this.offStack = player.getItemBySlot(EquipmentSlot.OFFHAND);
            this.helmetStack = player.getItemBySlot(EquipmentSlot.HEAD);
            this.chestplateStack = player.getItemBySlot(EquipmentSlot.CHEST);
            this.leggingsStack = player.getItemBySlot(EquipmentSlot.LEGS);
            this.bootsStack = player.getItemBySlot(EquipmentSlot.FEET);
            this.main = mainStack.getItem();
            this.off = offStack.getItem();
            this.helmet = helmetStack.getItem();
            this.chestplate = chestplateStack.getItem();
            this.leggings = leggingsStack.getItem();
            this.boots = bootsStack.getItem();
            this.driver = InitItem.buildDriver.get();
            updateTag();
            this.equieDriver = leggings.equals(driver);
        } else {
            this.mainStack = ItemStack.EMPTY;
            this.offStack = ItemStack.EMPTY;
            this.helmetStack = ItemStack.EMPTY;
            this.chestplateStack = ItemStack.EMPTY;
            this.leggingsStack = ItemStack.EMPTY;
            this.bootsStack = ItemStack.EMPTY;
            this.main = null;
            this.off = null;
            this.helmet = null;
            this.chestplate = null;
            this.leggings = null;
            this.boots = null;
            this.driver = null;
            this.equieDriver = false;
        }
    }

    protected enum ClientMessageEnum {
        Organic("key.kamenrider_build.clear_driver.organic_title"),
        Inorganic("key.kamenrider_build.clear_driver.inorganic_title"),
        Air("key.kamenrider_build.clear_driver.air");
        final String string;

        ClientMessageEnum(String string) {
            this.string = string;
        }
    }

    private void updateTag() {
        this.driverTag = leggingsStack.getTag();
        if (driverTag == null) {
            this.driverTag = leggingsStack.getOrCreateTag();
        }
    }

    protected void ClientMessage(ClientMessageEnum key) {
        this.ClientMessage(new TranslatableComponent(key.string), true);
    }

    protected void ClientMessage(Component key, boolean isAction) {
        player.displayClientMessage(key, isAction);
    }

    // 存储物品并转换为NBT形式
    protected CompoundTag savePiece(ItemStack item) {
        CompoundTag tag = new CompoundTag();
        if (!item.isEmpty()) {
            item.save(tag);
        }
        return tag;
    }
    protected ItemStack loadItem(CompoundTag parent, String key) {
        return parent.contains(key) ? ItemStack.of(parent.getCompound(key)) : ItemStack.EMPTY;
    }
    protected boolean isHenshin(){
        return helmet instanceof BuildArmor && chestplate instanceof BuildArmor && boots instanceof BuildArmor;
    }
}
