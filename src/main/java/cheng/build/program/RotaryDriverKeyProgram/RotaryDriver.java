package cheng.build.program.RotaryDriverKeyProgram;

import cheng.build.Build;
import cheng.build.api.IFullBottle;
import cheng.build.entity.EffectEntity;
import cheng.build.entity.BuildUpEffectEntity;
import cheng.build.init.InitItem;
import cheng.build.item.armor.BuildDriver;
import cheng.build.rider_syteam.BottleRegistry;
import cheng.build.rider_syteam.HenshinUtil;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class RotaryDriver extends RotaryDriverBase {
    private IHenshin iHenshin;

    private static DriverMode currentMode = DriverMode.IDLE;
    protected static int
            CurrunStartTime = 0,
            CurrunEndTime = 0;

    public RotaryDriver(){

    }

    /**
     * 开始摇动进行变身前摇
     */
    public void handleRoundStart() {
        Build.LOGGER.debug("摇动开始，当前模式：{}", currentMode);
        if (equieDriver)
            switch (currentMode) {
                case IDLE -> {
                    // 空闲状态 -> 开始变身
                    ((RotaryDriver)iHenshin).update(player);
                    iHenshin.startHenshin();
                }
                // 变身
                case HENSHIN_START -> {
                    // 变身中 -> 开始摇动（变身后）
                    ((RotaryDriver)iHenshin).update(player);
                    iHenshin.startRound();
                }
                default -> {
                    Build.LOGGER.warn("未知的状态：{}", currentMode);
                }
            }
    }

    /**
     * 摇动结束完成变身
     */
    public void handleRoundStop() {
        Build.LOGGER.debug("摇动停止，当前模式：{}", currentMode);
        if (equieDriver)
            switch (currentMode) {
                case HENSHIN_START:
                    // 完成变身
                    ((RotaryDriver)iHenshin).update(player);
                    iHenshin.stopHenshin();
                    break;

                case HENSHIN_LATER:
                    // 完成摇动（形态切换）
                    ((RotaryDriver)iHenshin).update(player);
                    iHenshin.stopRound();
                    break;

                default:
                    Build.LOGGER.warn("未知的状态：{}", currentMode);
            }
    }

    /**
     * 重置驱动器状态
     */
    public static void reset() {
        currentMode = DriverMode.IDLE;
        Build.LOGGER.debug("重置驱动器状态");
    }

    /**
     * 获取当前状态
     */
    public String getStatus() {
        return String.format("当前模式：%s，%s",
                currentMode,
                currentMode == DriverMode.HENSHIN_COMPLETE ? getCurrentFormInfo() : "");
    }

    protected BuildUpEffectEntity effect() {
        for (EffectEntity effectEntity : getEntity()) {
            if (effectEntity instanceof BuildUpEffectEntity entity && effectEntity.getOwner() == player)
                return entity;
        }
        return new BuildUpEffectEntity(player.level);
    }

    /**
     * 获取当前驱动器类型返回对应接口
     * @return IHenshin接口
     */
    private IHenshin getCurrentDriverReturnIHenshin() {
        BuildDriver buildDriver = InitItem.buildDriver.get();
        if (leggings.equals(buildDriver)) {
            return new BuildDriverHenshinContext();
        }
        return null;
    }

    public void setiHenshin() {
        this.iHenshin = getCurrentDriverReturnIHenshin();
    }

    /**
     * 设置状态
     */
    public static void setCurrentMode(DriverMode currentMode) {
        RotaryDriver.currentMode = currentMode;
    }

    public enum DriverMode {
        IDLE,               // 空闲(不变身)
        HENSHIN_START,      // 变身或切换形态摇动
        HENSHIN_COMPLETE,   // 变身中(过程)
        HENSHIN_LATER,      // 变身或切换形态摇动后
        HENSHIN_IDLE,       // (变身)空闲
        SKILL_START,        // 技能开始
        SKILL_COMPLETE,     // 使用技能中
        SKILL_STOP          // 结束技能
        ;
    }

    /**
     * 获取当前形态信息（调试用）
     */
    public String getCurrentFormInfo() {
        ItemStack organic = loadItem(driverTag, BuildDriver.organicMatter_item_Name);
        ItemStack inorganic = loadItem(driverTag, BuildDriver.inorganicMatter_item_Name);

        Optional<IFullBottle> organicBottle = findBottleByItem(organic);
        Optional<IFullBottle> inorganicBottle = findBottleByItem(inorganic);

        return String.format("有机物: %s, 无机物: %s",
                organicBottle.map(IFullBottle::getName).orElse("无"),
                inorganicBottle.map(IFullBottle::getName).orElse("无"));
    }

    private static final List<IFullBottle> BOTTLE_CACHE = BottleRegistry.getAllBottles();

    private Optional<IFullBottle> findBottleByItem(ItemStack stack) {
        if (stack.isEmpty()) return Optional.empty();
        return BOTTLE_CACHE.stream()
                .filter(b -> b.getFullBottle().equals(stack.getItem()))
                .findFirst();
    }

    private List<EffectEntity> getEntity() {
        HenshinUtil henshinUtil = new HenshinUtil();
        henshinUtil.update(player);
        return henshinUtil.getEntityList(EffectEntity.class, 4 / 2d);
    }
}
