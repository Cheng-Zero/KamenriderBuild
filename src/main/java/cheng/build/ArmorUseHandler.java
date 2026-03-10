package cheng.build;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

/**
 * 关于变身时存储盔甲的类
 */
public class ArmorUseHandler {
    private static final String HenshinArmorNbt = "Wizard_ArmorSave";
    public static void saveArmor(ServerPlayer player) {
        if (player == null || player.level.isClientSide()) return;

        CompoundTag armorData = new CompoundTag();

        armorData.put("helmet", saveArmorPiece(player.getInventory().getArmor(3)));
        armorData.put("chestplate", saveArmorPiece(player.getInventory().getArmor(2)));
        armorData.put("boots", saveArmorPiece(player.getInventory().getArmor(0)));
        player.getPersistentData().put(HenshinArmorNbt, armorData);
    }

    private static CompoundTag saveArmorPiece(ItemStack item) {
        CompoundTag tag = new CompoundTag();
        if (!item.isEmpty()) {
            item.save(tag);
        }
        return tag;
    }

    public static void loadArmor(ServerPlayer player) {
        if (!player.getPersistentData().contains(HenshinArmorNbt)) {
            return;
        }
        CompoundTag armorData = player.getPersistentData().getCompound(HenshinArmorNbt);

        player.getInventory().setItem(39, loadItem(armorData, "helmet"));
        player.getInventory().setItem(38, loadItem(armorData, "chestplate"));
        player.getInventory().setItem(36, loadItem(armorData, "boots"));
        // 强制同步到客户端
        player.inventoryMenu.broadcastChanges();
        player.getPersistentData().remove(HenshinArmorNbt);
    }
    private static ItemStack loadItem(CompoundTag parent, String key) {
        return parent.contains(key) ? ItemStack.of(parent.getCompound(key)) : ItemStack.EMPTY;
    }
}