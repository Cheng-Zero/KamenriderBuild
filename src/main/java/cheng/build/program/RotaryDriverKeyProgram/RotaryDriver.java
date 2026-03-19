package cheng.build.program.RotaryDriverKeyProgram;

import cheng.build.Build;
import cheng.build.api.IFullBottle;
import cheng.build.data.ABaseData;
import cheng.build.entity.EffectEntity;
import cheng.build.entity.BuildUpEffectEntity;
import cheng.build.init.InitItem;
import cheng.build.item.armor.BuildDriver;
import cheng.build.rider_syteam.BottleRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class RotaryDriver extends RotaryDriverBase {
    private IHenshin iHenshin;

    private static DriverMode currentMode = DriverMode.IDLE;
    protected static int
            CurrunStartTime = 0,
            CurrunEndTime = 0;

    public RotaryDriver(){
    }

    public void handleRoundStart() {
        Build.LOGGER.debug("摇动开始，当前模式：{}", currentMode);
        if (equieDriver)
            switch (currentMode) {
                case IDLE -> {
                    // 空闲状态 -> 开始变身
                    currentMode = DriverMode.HENSHIN_START;
                    ((RotaryDriver)iHenshin).update(player);
                    iHenshin.startHenshin();
                    break;
                }
                case HENSHIN_START -> {
                    // 变身中 -> 开始摇动（变身后）
                    currentMode = DriverMode.HENSHIN_LATER;
                    ((RotaryDriver)iHenshin).update(player);
                    iHenshin.startRound();
                    break;
                }
                default -> {
                    Build.LOGGER.warn("未知的状态：{}", currentMode);
                }
            }
    }

    public void handleRoundStop() {
        Build.LOGGER.debug("摇动停止，当前模式：{}", currentMode);
        if (equieDriver)
            switch (currentMode) {
                case HENSHIN_START:
                    // 完成变身
                    ((RotaryDriver)iHenshin).update(player);
                    iHenshin.stopHenshin();
                    currentMode = DriverMode.HENSHIN_COMPLETE;
                    break;

                case HENSHIN_LATER:
                    // 完成摇动（形态切换）
                    ((RotaryDriver)iHenshin).update(player);
                    iHenshin.stopRound();
                    currentMode = DriverMode.HENSHIN_COMPLETE;
                    break;

                default:
                    Build.LOGGER.warn("未知的状态：{}", currentMode);
            }
    }

    public static void reset() {
        currentMode = DriverMode.IDLE;
        Build.LOGGER.debug("重置驱动器状态");
    }

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

    private List<EffectEntity> getEntity() {
        final Vec3 playerXYZ = new Vec3(player.getX(), player.getY(), player.getZ());
        AABB aabb = new AABB(playerXYZ, playerXYZ).inflate(4 / 2d);
        // 获取世界实体类型(Class)  目标实体.class    获取范围         条件判断具体目标
        return player.level.getEntitiesOfClass(EffectEntity.class, aabb, e -> true)
                // 转换成 数据流
                .stream()
                // 距离排序（影响处理实体先后顺序）
                .sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(playerXYZ)))
                // 转换为List列表
                .toList();
    }

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

    public static void setCurrentMode(DriverMode currentMode) {
        RotaryDriver.currentMode = currentMode;
    }

    public enum DriverMode {
        IDLE,               // 空闲
        HENSHIN_START,      // 变身中（第一次摇动）
        HENSHIN_LATER,      // 变身后（后续摇动）
        HENSHIN_COMPLETE    // 变身完成
    }

    // 获取当前形态信息（调试用）
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
}
