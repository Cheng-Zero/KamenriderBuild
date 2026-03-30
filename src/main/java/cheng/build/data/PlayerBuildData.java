package cheng.build.data;

import cheng.build.Build;
import cheng.build.item.armor.base.BaseDriver;
import cheng.build.item.armor.base.BuildArmor;
import cheng.build.network.PlayerBuildDataSyncPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

import java.util.Random;

public final class PlayerBuildData {
    private final Player player;
    static Random random = new Random();
    private TransformMode currentMode = TransformMode.IDLE; // 当前状态
    public double hazard_level = HazardLevel();

    public PlayerBuildData(Player player){
        this.player = player;
    }

    // 获取玩家
    public Player getPlayer() { return player; }
    // 获取状态
    public TransformMode getCurrentMode() { return currentMode; }
    public double getHazardLevel() { return hazard_level; }
    // 判断穿戴驱动器
    public boolean isEquieDriver(){return player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof BaseDriver;}
    // 处于变身
    public boolean isHenshin(){
        var helmet = player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof BuildArmor;
        var chestplate = player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof BuildArmor;
        var boots = player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof BuildArmor;
        return helmet && chestplate && boots;
    }
    // 设置状态
    public void setCurrentMode(TransformMode mode) { this.currentMode = mode; syncToClient();}
    public void setHazardLevel(double hazardLevel) { this.hazard_level = hazardLevel; syncToClient();}
    // 获取护腿Tag标签
    public CompoundTag getDriverTag() {
        if (isEquieDriver())
            return player.getItemBySlot(EquipmentSlot.LEGS).getTag();
        return new CompoundTag();
    }
    // 驱动器状态使用
    public boolean isDriverInUse() {
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        return !legs.isEmpty() && legs.getTag() != null && legs.getTag().getBoolean("isUse");
    }
    // 设置驱动器状态
    public void setDriverInUse(boolean inUse) {
        if (!player.level.isClientSide()) {
            ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
            if (legs.getTag() == null) {
                legs.getOrCreateTag().putBoolean("isUse", inUse);
                return;
            }
            legs.getTag().putBoolean("isUse", inUse);
        }
    }
    // 存储物品并转换为NBT形式
    public CompoundTag savePiece(ItemStack item) {
        CompoundTag tag = new CompoundTag();
        if (!item.isEmpty()) {
            item.save(tag);
        }
        return tag;
    }
    public enum ClientMessageEnum {
        Organic("key.kamenrider_build.clear_driver.organic_title"),
        Inorganic("key.kamenrider_build.clear_driver.inorganic_title"),
        Air("key.kamenrider_build.clear_driver.air");
        final String string;

        ClientMessageEnum(String string) {
            this.string = string;
        }
    }

    public void ClientMessage(ClientMessageEnum key) {
        this.ClientMessage(new TranslatableComponent(key.string), true);
    }

    public void ClientMessage(Component key, boolean isAction) {
        player.displayClientMessage(key, isAction);
    }

    // ========== NBT 保存/加载 ==========
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putString("mode", currentMode.name());
        tag.putDouble("hazard_level", hazard_level);
        return tag;
    }

    public void load(CompoundTag tag) {
        currentMode = TransformMode.valueOf(tag.getString("mode"));
        hazard_level = tag.getDouble("hazard_level");
    }

    // 状态枚举
    public enum TransformMode {
        IDLE,               // 空闲(不变身)
        HENSHIN_COMPLETE,   // 变身中(过程)
        HENSHIN_IDLE,       // (变身)空闲
        SKILL_COMPLETE,     // 使用技能中
    }
    /**
     * 初始危险等级计算
     */
    double HazardLevel() {
        while (true) {
            // 0.0 - 1.0
            double finalValue = random.nextDouble() * 10;
            // 值 大于1.0 同时 小于3.0 时 返回值
            if (finalValue <= 3 && finalValue > 1) {
                // 只取1位小数
                return Math.round(finalValue);
            }
        }
    }
    public void syncToClient() {
        if (player instanceof ServerPlayer serverPlayer) {
            Build.PACKET_HANDLER.send(
                    PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new PlayerBuildDataSyncPacket(this)
            );
        }
    }
}
