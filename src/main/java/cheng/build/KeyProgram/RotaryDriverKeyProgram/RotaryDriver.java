package cheng.build.KeyProgram.RotaryDriverKeyProgram;

import cheng.build.Build;
import cheng.build.data.ABaseData;
import cheng.build.entity.EffectEntity;
import cheng.build.entity.BuildUpEffectEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public class RotaryDriver extends ABaseData {
    protected static int
            CurrunStartTime = 0,
            CurrunEndTime = 0;
    protected static final String
            Animation_start = "start",
            Animation_standby_start = "standby_start",
            Animation_standbying = "standbying",
            Animation_build_up = "build_up",
            Animation_build_up_close = "build_up_close",
            Animation_build_up_over = "build_up_over";

    public void on() {
        if (equieDriver) {
            Build.LOGGER.info("使用成功");
            if (isHenshin()){
                // 变身之后的方法调用
                HenshinLater henshinLater = new HenshinLater();
                // 数据同步
                henshinLater.update(player);
                henshinLater.round();
            }
            else {
                // 变身之前的方法调用
                HenshinBefore henshinBefore = new HenshinBefore();
                henshinBefore.update(player);
                henshinBefore.Henshin();
            }
        }
    }
    public void off() {
        if (equieDriver) {
            Build.LOGGER.info("使用成功");
            if (isHenshin()){
                // 变身之后的方法调用
                HenshinLater henshinLater = new HenshinLater();
                // 数据同步
                henshinLater.update(player);
                henshinLater.stop();
            }
            else {
                // 变身之前的方法调用
                HenshinBefore henshinBefore = new HenshinBefore();
                henshinBefore.update(player);
                henshinBefore.stop();
            }
        }
    }

    protected BuildUpEffectEntity effect(){
        for (EffectEntity effectEntity : getEntity()) {
            if (effectEntity instanceof BuildUpEffectEntity entity && effectEntity.getOwner() == player)
                return entity;
        }
        return new BuildUpEffectEntity(player.level);
    }

    private List<EffectEntity> getEntity(){
        final Vec3 playerXYZ = new Vec3(player.getX(), player.getY(), player.getZ());
        AABB aabb = new AABB(playerXYZ,playerXYZ).inflate(4/2d);
        // 获取世界实体类型(Class)  目标实体.class    获取范围         条件判断具体目标
        return player.level.getEntitiesOfClass(EffectEntity.class, aabb, e -> true)
                // 转换成 数据流
                .stream()
                // 距离排序（影响处理实体先后顺序）
                .sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(playerXYZ)))
                // 转换为List列表
                .toList();
    }
}
