package cheng.build.keybingds;

import cheng.build.KeyProgram.ShakeBottle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
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
        ShakeBottle shakeBottle = new ShakeBottle();
        if (world.hasChunkAt(entity.blockPosition())) {
            shakeBottle.update(entity);
            if (type == 0)
                shakeBottle.on();
            if (type==1)
                shakeBottle.off();
        }
    }
}
