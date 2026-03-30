package cheng.build.program.RotaryDriverKeyProgram;

import cheng.build.Build;
import cheng.build.api.IFullBottle;
import cheng.build.data.DataManager;
import cheng.build.data.PlayerBuildData;
import cheng.build.entity.EffectEntity;
import cheng.build.entity.BuildUpEffectEntity;
import cheng.build.init.InitItem;
import cheng.build.item.armor.BuildDriver;
import cheng.build.rider_syteam.BuildRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class RotaryDriver extends RotaryDriverBase {
    private IHenshin iHenshin;
    PlayerBuildData data;
    PlayerBuildData.TransformMode currentMode;

    protected int
            CurrunStartTime = 0,
            CurrunEndTime = 0;

    public RotaryDriver() {
    }

    /**
     * 开始摇动进行变身前摇
     */
    public void handleRoundStart() {
        data = DataManager.get(player);
        if (data.isEquieDriver()) {
            currentMode = data.getCurrentMode();
            ((RotaryDriver) iHenshin).update(player);

            Build.LOGGER.debug("摇动开始，当前模式：{}", currentMode);
            switch (currentMode) {
                // 空闲状态 -> 开始变身
                case IDLE -> iHenshin.startHenshin();
                // 完成变身
                case HENSHIN_IDLE -> iHenshin.startRound();
                default -> Build.LOGGER.warn("未知的状态：{}", currentMode);
            }
        }
    }
    /**
     * 开始摇动进行变身前摇
     */
    public void handleRoundStop() {
        data = DataManager.get(player);
        if (data.isEquieDriver()) {
            currentMode = data.getCurrentMode();
            ((RotaryDriver) iHenshin).update(player);

            Build.LOGGER.debug("摇动结束，当前模式：{}", currentMode);
            switch (currentMode) {
                // 变身中 -> 开始摇动（变身后）
                case HENSHIN_COMPLETE -> iHenshin.stopHenshin();
                // 完成摇动（形态切换）
                case SKILL_COMPLETE -> iHenshin.stopRound();
                default -> Build.LOGGER.warn("未知的状态：{}", currentMode);
            }
        }
    }

    protected BuildUpEffectEntity effect() {
        for (EffectEntity effectEntity : getEntityList(EffectEntity.class, 4 / 2d)) {
            if (effectEntity instanceof BuildUpEffectEntity entity && effectEntity.getOwner() == player)
                return entity;
        }
        return new BuildUpEffectEntity(player.level);
    }

    /**
     * 获取当前驱动器类型返回对应接口
     *
     * @return IHenshin接口
     */
    private IHenshin getCurrentDriverReturnIHenshin() {
        BuildDriver buildDriver = InitItem.buildDriver.get();
        Item leggings = player.getItemBySlot(EquipmentSlot.LEGS).getItem();
        if (leggings.equals(buildDriver)) {
            return new BuildDriverHenshinContext();
        }
        return null;
    }

    public void setiHenshin() {
        this.iHenshin = getCurrentDriverReturnIHenshin();
    }


    /**
     * 获取当前形态信息（调试用）
     */
    public String getCurrentFormInfo() {
        PlayerBuildData data = DataManager.get(player);
        CompoundTag driverTag = data.getDriverTag();
        ItemStack organic = loadItem(driverTag, BuildDriver.organicMatter_item_Name);
        ItemStack inorganic = loadItem(driverTag, BuildDriver.inorganicMatter_item_Name);

        Optional<IFullBottle> organicBottle = findBottleByItem(organic);
        Optional<IFullBottle> inorganicBottle = findBottleByItem(inorganic);

        return String.format("有机物: %s, 无机物: %s",
                organicBottle.map(IFullBottle::getName).orElse("无"),
                inorganicBottle.map(IFullBottle::getName).orElse("无"));
    }

    private static final List<IFullBottle> BOTTLE_CACHE = BuildRegistry.getAllBottles();

    private Optional<IFullBottle> findBottleByItem(ItemStack stack) {
        if (stack.isEmpty()) return Optional.empty();
        return BOTTLE_CACHE.stream()
                .filter(b -> b.getFullBottle().equals(stack.getItem()))
                .findFirst();
    }
}
