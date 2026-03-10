package cheng.build;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class ItemHelper {
    // =====================NBT数据=========================

    /**
     * 获取物品栈NBT数据
     * (数据类型Double的NBT)
     * 注意不要获取没有的NBT数据
     * 会出现空值崩溃
     *
     * @param itemStack 物品栈实例
     * @param pNbtName  Nbt名称
     */
    public static double getDoubleNbt(ItemStack itemStack, String pNbtName) {
        return itemStack.getTag().getDouble(pNbtName);
    }
    /**
     * 获取物品栈NBT数据
     * (数据类型Int的NBT)
     * 注意不要获取没有的NBT数据
     * 会出现空值崩溃
     *
     * @param itemStack 物品栈实例
     * @param pNbtName  Nbt名称
     */
    public static int getIntNbt(ItemStack itemStack, String pNbtName) {
        return  itemStack.getTag().getInt(pNbtName);
    }

    /**
     * * 获取物品栈NBT数据
     * (数据类型String的NBT)
     * 注意不要获取没有的NBT数据
     * 会出现空值崩溃
     *
     * @param itemStack 物品栈实例
     * @param pNbtName  Nbt名称
     */
    public static String getStringNbt(ItemStack itemStack, String pNbtName) {
        return itemStack.getTag().getString(pNbtName);
    }

    /**
     * * 获取物品栈NBT数据
     * (数据类型Boolean的NBT)
     * 注意不要获取没有的NBT数据
     * 会出现空值崩溃
     *
     * @param itemStack 物品栈实例
     * @param pNbtName  Nbt名称
     */
    public static boolean getBooleanNbt(ItemStack itemStack, String pNbtName) {
        return itemStack.getTag().getBoolean(pNbtName);
    }

    /**
     * 设置物品栈NBT数据
     * (数据类型Double的NBT)
     *
     * @param itemStack 物品栈实例
     * @param pNbtName  Nbt名称
     * @param number    数字
     */
    public static void putDoubleNbt(ItemStack itemStack, String pNbtName, double number) {
        itemStack.getOrCreateTag().putDouble(pNbtName, number);
    }

    /**
     * 设置物品栈NBT数据
     * (数据类型String的NBT)
     *
     * @param itemStack 物品栈实例
     * @param pNbtName  Nbt名称
     * @param name      字符串
     */
    public static void putStringNbt(ItemStack itemStack, String pNbtName, String name) {
        itemStack.getOrCreateTag().putString(pNbtName, name);
    }

    /**
     * 设置物品栈NBT数据
     * (数据类型boolean的NBT)
     *
     * @param itemStack 物品栈实例
     * @param pNbtName  Nbt名称
     * @param Boolean   布尔值
     */
    public static void putBooleanNbt(ItemStack itemStack, String pNbtName, boolean Boolean) {
        itemStack.getOrCreateTag().putBoolean(pNbtName, Boolean);
    }
    // ======================给予清除物品=========================

    /**
     * 给予 1个 物品
     *
     * @param player 玩家实例
     * @param item   物品实例
     */
    public static void giveItem(Player player, Item item) {
        ItemStack itemStack = new ItemStack(item);
        itemStack.setCount(1);
        ItemHandlerHelper.giveItemToPlayer(player, itemStack);
        player.getInventory().setChanged();
    }

    /**
     * 给予 自定义数量 的 物品
     *
     * @param player 玩家实例
     * @param item   物品实例
     * @param count  数量
     */
    public static void giveItem(Player player, Item item, int count) {
        ItemStack itemStack = new ItemStack(item);
        itemStack.setCount(count);
        ItemHandlerHelper.giveItemToPlayer(player, itemStack);
        player.getInventory().setChanged();
    }

    /**
     * 给予 物品栈 的 物品
     *
     * @param player    玩家实例
     * @param itemStack 物品栈实例
     */
    public static void giveItem(Player player, ItemStack itemStack) {
        itemStack.setCount(1);
        ItemHandlerHelper.giveItemToPlayer(player, itemStack);
        player.getInventory().setChanged();
    }

    /**
     * 给予 物品栈×数量 的 物品
     *
     * @param player    玩家实例
     * @param itemStack 物品栈实例
     * @param count     数量
     */
    public static void giveItem(Player player, ItemStack itemStack, int count) {
        itemStack.setCount(count);
        ItemHandlerHelper.giveItemToPlayer(player, itemStack);
        player.getInventory().setChanged();
    }

    /**
     * 清除 玩家实例 背包中 物品实例 1个
     *
     * @param player 玩家实例
     * @param item   物品实例
     */
    public static void removeItem(Player player, Item item) {
        player.getInventory().clearOrCountMatchingItems(p -> item == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
        player.getInventory().setChanged();
    }

    /**
     * 清除 玩家实例 背包中 物品实例 自定义数量 个
     *
     * @param player 玩家实例
     * @param item   物品实例
     * @param count  数量
     */
    public static void removeItem(Player player, Item item, int count) {
        player.getInventory().clearOrCountMatchingItems(p -> item == p.getItem(), count, player.inventoryMenu.getCraftSlots());
        player.getInventory().setChanged();
    }

    /**
     * 清除 玩家实例 背包中 物品栈实例 1个
     *
     * @param player    玩家实例
     * @param itemStack 物品栈实例
     */
    public static void removeItem(Player player, ItemStack itemStack) {
        itemStack.shrink(1);
        player.getInventory().setChanged();
    }

    /**
     * 清除 玩家实例 背包中 物品栈实例 自定义数量 个
     *
     * @param player    玩家实例
     * @param itemStack 物品栈实例
     * @param count     数量
     */
    public static void removeItem(Player player, ItemStack itemStack, int count) {
        itemStack.shrink(count);
        player.getInventory().setChanged();
    }

    /**
     * 清除全部物品
     *
     * @param player 玩家实例
     */
    public static void clearAllItem(Player player) {
        player.getInventory().clearContent();
    }
    // =====================冷却============================

    /**
     * 设置 玩家实例 的 物品实例 冷却时间
     *
     * @param player 玩家实例
     * @param item   物品实例
     * @param pTick  冷却时间刻
     * @return 设置物品冷却
     */
    public static ItemCooldowns setCooldown(Player player, Item item, int pTick) {
        ItemCooldowns Cooldowns = player.getCooldowns();
        Cooldowns.addCooldown(item, pTick);
        return Cooldowns;
    }

    /**
     * 设置 玩家实例 的 物品栈实例 冷却时间
     *
     * @param player    玩家实例
     * @param itemStack 物品栈实例
     * @param pTick     冷却时间刻
     * @return 设置物品冷却
     */
    public static ItemCooldowns setCooldown(Player player, ItemStack itemStack, int pTick) {
        ItemCooldowns Cooldowns = player.getCooldowns();
        Cooldowns.addCooldown(itemStack.getItem(), pTick);
        return Cooldowns;
    }

    /**
     * 物品是否处于冷却的判断
     *
     * @param serverPlayer 玩家实例
     * @param item         物品实例
     * @return 是否处于冷却
     */
    public static boolean isCooldown(ServerPlayer serverPlayer, Item item) {
        ItemCooldowns serverPlayerCooldowns = serverPlayer.getCooldowns();
        return serverPlayerCooldowns.isOnCooldown(item);
    }

    /**
     * 物品是否处于冷却的判断
     *
     * @param serverPlayer 玩家实例
     * @param itemStack    物品实例
     * @return 是否处于冷却
     */
    public static boolean isCooldown(ServerPlayer serverPlayer, ItemStack itemStack) {
        ItemCooldowns serverPlayerCooldowns = serverPlayer.getCooldowns();
        return serverPlayerCooldowns.isOnCooldown(itemStack.getItem());
    }

    // =====================兼容性使用============================

    public static ItemStack modItem(String modId, String itemId) {
        return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modId + ":" + itemId)));
    }

    // =================================================

    /**
     * 获取 目标 位置物品
     *
     * @param entity        事件实体实例
     * @param equipmentSlot 栏位
     * @return 物品栈实例
     */
    public static ItemStack getEquipmentSlotItem(LivingEntity entity, EquipmentSlot equipmentSlot) {
        if (entity instanceof LivingEntity)
            return entity.getItemBySlot(equipmentSlot);
        else
            return ItemStack.EMPTY;
    }
    /**
     * 获取 目标 位置物品
     *
     * @param livingEntity        事件实体实例
     * @param equipmentSlot 栏位
     * @return 物品栈实例
     */
    public static @NotNull Item getEquipmentSlotItem(EquipmentSlot equipmentSlot, LivingEntity livingEntity) {
        if (livingEntity instanceof LivingEntity)
            return livingEntity.getItemBySlot(equipmentSlot).getItem();
        return ItemStack.EMPTY.getItem();
    }

    /**
     * 是否拥有 物品实例
     *
     * @param player 玩家实例
     * @param item   物品实例
     * @return true 或 false
     */
    public static boolean containsItem(@NotNull Player player, Item item) {
        return player.getInventory().contains(new ItemStack(item));
    }

    /**
     * 检测是否是目标盔甲(头->身->腿->鞋)
     *
     * @param entity 实体实例
     * @param items  物品实例
     * @return 是否穿戴目标物品，返回值true或false
     */
    public static boolean isWearingAny(Entity entity, Item[] items) {
        if (items != null) {
            if (entity instanceof Player player) {
                ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
                ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
                ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
                ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);

                // 提取物品，安全处理null
                Item helmetItem = helmet != null ? helmet.getItem() : null;
                Item chestItem = chest != null ? chest.getItem() : null;
                Item legsItem = legs != null ? legs.getItem() : null;
                Item bootsItem = boots != null ? boots.getItem() : null;

                for (Item item : items) {
                    if (helmetItem == item || chestItem == item || legsItem == item || bootsItem == item)
                        return true;
                }
            } else if (entity instanceof LivingEntity living) {
                ItemStack helmet = living.getItemBySlot(EquipmentSlot.HEAD);
                ItemStack chest = living.getItemBySlot(EquipmentSlot.CHEST);
                ItemStack legs = living.getItemBySlot(EquipmentSlot.LEGS);
                ItemStack boots = living.getItemBySlot(EquipmentSlot.FEET);

                // 提取物品，安全处理null
                Item helmetItem = helmet != null ? helmet.getItem() : null;
                Item chestItem = chest != null ? chest.getItem() : null;
                Item legsItem = legs != null ? legs.getItem() : null;
                Item bootsItem = boots != null ? boots.getItem() : null;

                for (Item item : items) {
                    return helmetItem == item && chestItem == item && legsItem == item && bootsItem == item;
                }
            }
        }
        return false;
    }
}
