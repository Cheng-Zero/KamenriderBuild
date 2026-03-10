package cheng.build.keybingds;

import cheng.build.Build;
import cheng.build.animation.PlayerAnimationUtil;
import cheng.build.bottle.Bottle;
import cheng.build.bottle.FullBottle;
import cheng.build.init.InitItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class ShakeBottleMessageKey {
    int type;
    int pressedms;

    public ShakeBottleMessageKey(int type, int pressedms) {
        this.type = type;
        this.pressedms = pressedms;
    }

    public ShakeBottleMessageKey(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
        this.pressedms = buffer.readInt();
    }

    public static void buffer(ShakeBottleMessageKey message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
        buffer.writeInt(message.pressedms);
    }

    public static void handler(ShakeBottleMessageKey message, Supplier<NetworkEvent.Context> contextSupplier) {
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
                on(entity);
            if (type==1)
                off(entity);
        }
    }
    public static int CurrunStartTime = 0;
    public static int CurrunEndTime = 0;
    private static void on(Player player) {
        Item mainitem = player.getItemBySlot(EquipmentSlot.MAINHAND).getItem();
        Item offitem = player.getItemBySlot(EquipmentSlot.OFFHAND).getItem();
        boolean onMain = mainitem instanceof Bottle;
        boolean onOff = offitem instanceof Bottle;
        Build.LOGGER.info("摇晃满装瓶中");
        CurrunStartTime = (int) player.level.getGameTime();
        if (onMain && onOff)
            shakeMainOffHandBottle(player);
        else if (onMain)
            shakeMainHandBottle(player);
        else if (onOff)
            shakeOffHandBottle(player);
    }
    private static void off(Player player) {
        Build.LOGGER.info("停止摇晃");
        playAni(player, "ddd", false);
        Item mainitem = player.getItemBySlot(EquipmentSlot.MAINHAND).getItem();
        Item offitem = player.getItemBySlot(EquipmentSlot.OFFHAND).getItem();
        boolean onMain = mainitem instanceof FullBottle;
        boolean onOff = offitem instanceof FullBottle;
        CurrunEndTime = (int) player.level.getGameTime();
        int time =CurrunEndTime-CurrunStartTime;
        if (onMain && onOff) {
            if (mainitem instanceof FullBottle fullBottle)
                fullBottle.applyEffect(player, time);
            if (offitem instanceof FullBottle fullBottle)
                fullBottle.applyEffect(player, time);
        } else if (onMain) {
            if (mainitem instanceof FullBottle fullBottle)
                fullBottle.applyEffect(player, time);
        } else if (onOff) {
            if (offitem instanceof FullBottle fullBottle)
                fullBottle.applyEffect(player, time);
        }
    }
    private static void shakeMainHandBottle(Player player){
        playAni(player,"ddd",true);
    }
    private static void shakeOffHandBottle(Player player){
        playAni(player,"ddd",true);
    }
    private static void shakeMainOffHandBottle(Player player){
        playAni(player,"ddd",true);
    }
    private static void playAni(Player player,String a,boolean s){
        if (player instanceof ServerPlayer serverPlayer)
            PlayerAnimationUtil.playanimation(serverPlayer,a,s);
    }
}
