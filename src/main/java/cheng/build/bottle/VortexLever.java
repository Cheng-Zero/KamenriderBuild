package cheng.build.bottle;

import cheng.build.Build;
import cheng.build.DelayedTask;
import cheng.build.player_animation.PlayerAnimationUtil;
import cheng.build.entity.BuildUpEffectEntity;
import cheng.build.entity.HHHHNiceEntity;
import cheng.build.init.InitEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class VortexLever extends ABaseData{
    private static final int
            CurrunStartTime = 0,
            CurrunEndTime = 0;
    private static final String
            Animation_start = "start",
            Animation_standby_start = "standby_start",
            Animation_standbying = "standbying",
            Animation_build_up = "build_up",
            Animation_build_up_close = "build_up_close",
            Animation_build_up_over = "build_up_over";

    public void on(Level world) {
        if (equieDriver && hasBuildUpEntity(world)) {
            Build.LOGGER.info("使用成功");
            if (player instanceof ServerPlayer serverPlayer)
                PlayerAnimationUtil.playanimation(serverPlayer, "ddd", true);
            driverTag.putBoolean("isUse", true);
            BuildUpEffectEntity e = new BuildUpEffectEntity(InitEntity.build_up.get(), world);
            e.setAnimation(Animation_start);
            BuildUpEffectEntity.addEntity(e, player);
            DelayedTask.chain(player.level)
                    .then(40, () -> areYouRealdy(world, Animation_standby_start))
                    .then(1, () -> areYouRealdy(world, Animation_standbying))
                    .start();
        }
    }
    public void off(LevelAccessor world) {
        ItemStack driv = player.getItemBySlot(EquipmentSlot.LEGS);
        CompoundTag t = Objects.requireNonNull(driv.getTag());
        if (equieDriver && t.getBoolean("isUse")){
            Build.LOGGER.info("按键抬起成功");
            if (player instanceof ServerPlayer serverPlayer)
                PlayerAnimationUtil.playanimation(serverPlayer,"ddd",false);
            DelayedTask.chain(player.level)
                    .then(45, ()->areYouRealdy(world,Animation_build_up))
                    .then(10, ()->areYouRealdy(world,Animation_build_up_close))
                    .then(30,()->areYouRealdy(world,Animation_build_up_over))
                    .then(32,()->over(world))
                    .start();
            driverTag.putBoolean("isUse",false);
        }
    }
    private void areYouRealdy(LevelAccessor world,String animationName){
        for (Entity entityiterator : e(world)) {
            if (entityiterator instanceof BuildUpEffectEntity build && build.getOwner() == player)
                build.getEntityData().set(HHHHNiceEntity.Animation,animationName);
        }
    }
    private void over(LevelAccessor world){
        for (Entity entityiterator : e(world)) {
            if (entityiterator instanceof BuildUpEffectEntity build && build.getOwner() == player)
                build.discard();
        }
    }
    private boolean hasBuildUpEntity(LevelAccessor world){
        for (HHHHNiceEntity e : e(world))
            if (e instanceof BuildUpEffectEntity build&&build.getOwner()==player)
                return false;
        return true;
    }
    private List<HHHHNiceEntity> e(LevelAccessor world){
        final Vec3 _center = new Vec3(player.getX(), player.getY(), player.getZ());
        return world.getEntitiesOfClass(HHHHNiceEntity.class, new AABB(_center, _center).inflate(4 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
    }
}
