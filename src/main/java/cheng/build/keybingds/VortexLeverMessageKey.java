package cheng.build.keybingds;

import cheng.build.Build;
import cheng.build.DelayedTask;
import cheng.build.animation.PlayerAnimationUtil;
import cheng.build.entity.BuildUpEffectEntity;
import cheng.build.entity.HHHHNiceEntity;
import cheng.build.init.InitEntity;
import cheng.build.init.InitItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public class VortexLeverMessageKey {
    int type;
    int pressedms;

    public VortexLeverMessageKey(int type, int pressedms) {
        this.type = type;
        this.pressedms = pressedms;
    }

    public VortexLeverMessageKey(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
        this.pressedms = buffer.readInt();
    }

    public static void buffer(VortexLeverMessageKey message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
        buffer.writeInt(message.pressedms);
    }

    public static void handler(VortexLeverMessageKey message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            pressAction(Objects.requireNonNull(context.getSender()), message.type, message.pressedms);
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player entity, int type, int pressedms) {
        Level world = entity.level;
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        if (world.hasChunkAt(entity.blockPosition())) {
            if (type == 0)
                on(world,entity);
            if (type==1)
                off(world,entity);
        }
    }
    private static final int CurrunStartTime = 0;
    private static final int CurrunEndTime = 0;
    private static void on(Level world,Player player) {
        ItemStack driv = player.getItemBySlot(EquipmentSlot.LEGS);
        CompoundTag t = Objects.requireNonNull(driv.getTag());
        if (driv.getItem()== InitItem.buildDriver.get()&& hasBuildUpEntity(world,player))
        {
            Build.LOGGER.info("使用成功");
            if (player instanceof ServerPlayer serverPlayer)
                PlayerAnimationUtil.playanimation(serverPlayer,"ddd",true);
            driv.getTag().putBoolean("isUse",true);
            BuildUpEffectEntity e = new BuildUpEffectEntity(InitEntity.build_up.get(),world);
            e.setAnimation("start");
            BuildUpEffectEntity.addEntity(e,player);
            UUID c = DelayedTask.chain(player.level)
                    .then(40, ()->areYouRealdy(world,player,"standby_start"))
                    .then(1,()->areYouRealdy(world,player,"standbying"))
                    .start();
        }
    }
    private static void off(LevelAccessor world,Player player) {
        ItemStack driv = player.getItemBySlot(EquipmentSlot.LEGS);
        CompoundTag t = Objects.requireNonNull(driv.getTag());
        if (driv.getItem()== InitItem.buildDriver.get()&& t.getBoolean("isUse")){
            Build.LOGGER.info("按键抬起成功");
            if (player instanceof ServerPlayer serverPlayer)
                PlayerAnimationUtil.playanimation(serverPlayer,"ddd",false);
            DelayedTask.chain(player.level)
                    .then(45, ()->areYouRealdy(world,player,"build_up"))
                    .then(10, ()->areYouRealdy(world,player,"build_up_close"))
                    .then(30,()->areYouRealdy(world,player,"build_up_over"))
                    .then(32,()->over(world,player))
                    .start();
            driv.getTag().putBoolean("isUse",false);
        }
    }
    private static void areYouRealdy(LevelAccessor world,Player player,String animationName){
        for (Entity entityiterator : e(world, player)) {
            if (entityiterator instanceof BuildUpEffectEntity build && build.getOwner() == player)
                build.getEntityData().set(HHHHNiceEntity.Animation,animationName);
        }
    }
    private static void over(LevelAccessor world,Player player){
        for (Entity entityiterator : e(world, player)) {
            if (entityiterator instanceof BuildUpEffectEntity build && build.getOwner() == player)
                build.discard();
        }
    }
    private static boolean hasBuildUpEntity(LevelAccessor world,Player player){
        for (HHHHNiceEntity e : e(world, player))
            if (e instanceof BuildUpEffectEntity build&&build.getOwner()==player)
                return false;
        return true;
    }
    private static List<HHHHNiceEntity> e(LevelAccessor world,Player player){
        final Vec3 _center = new Vec3(player.getX(), player.getY(), player.getZ());
        return world.getEntitiesOfClass(HHHHNiceEntity.class, new AABB(_center, _center).inflate(4 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
    }
}
