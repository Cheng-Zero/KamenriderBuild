package cheng.build;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * 关于变身时存储盔甲的类
 */
public class ArmorUseHandler {
    private static final String
            HenshinArmorNbt = "BuildArmorSave",
            helmet = "helmet",
            chestplate = "chestplate",
            boots = "boots";
    public static void saveArmor(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            if (serverPlayer == null || serverPlayer.level.isClientSide()) return;

            CompoundTag armorData = new CompoundTag();

            armorData.put(helmet, saveArmorPiece(serverPlayer.getInventory().getArmor(3)));
            armorData.put(chestplate, saveArmorPiece(serverPlayer.getInventory().getArmor(2)));
            armorData.put(boots, saveArmorPiece(serverPlayer.getInventory().getArmor(0)));
            serverPlayer.getPersistentData().put(HenshinArmorNbt, armorData);
        }
    }

    private static CompoundTag saveArmorPiece(ItemStack item) {
        CompoundTag tag = new CompoundTag();
        if (!item.isEmpty()) {
            item.save(tag);
        }
        return tag;
    }

    public static void loadArmor(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            if (!serverPlayer.getPersistentData().contains(HenshinArmorNbt)) {
                return;
            }
            CompoundTag armorData = serverPlayer.getPersistentData().getCompound(HenshinArmorNbt);

            serverPlayer.getInventory().setItem(39, loadItem(armorData, helmet));
            serverPlayer.getInventory().setItem(38, loadItem(armorData, chestplate));
            serverPlayer.getInventory().setItem(36, loadItem(armorData, boots));
            // 强制同步到客户端
            serverPlayer.inventoryMenu.broadcastChanges();
            serverPlayer.getPersistentData().remove(HenshinArmorNbt);
        }
    }
    private static ItemStack loadItem(CompoundTag parent, String key) {
        return parent.contains(key) ? ItemStack.of(parent.getCompound(key)) : ItemStack.EMPTY;
    }
}