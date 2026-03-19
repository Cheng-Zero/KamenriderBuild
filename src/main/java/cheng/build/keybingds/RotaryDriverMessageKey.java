package cheng.build.keybingds;

import cheng.build.program.RotaryDriverKeyProgram.RotaryDriver;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class RotaryDriverMessageKey extends ModKeybindings{

    public RotaryDriverMessageKey(int type, int pressedms) {
        super(type, pressedms);
    }

    public RotaryDriverMessageKey(FriendlyByteBuf buffer) {
        super(buffer);
    }

    public static void buffer(RotaryDriverMessageKey message, FriendlyByteBuf buffer) {
        message.encode(buffer);
    }

    public static void handler(RotaryDriverMessageKey message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            pressAction(Objects.requireNonNull(context.getSender()), message.getType(), message.getPressedms());
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player entity, int type, int pressedms) {
        Level world = entity.level;
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        if (world.hasChunkAt(entity.blockPosition())) {
            RotaryDriver rotaryDriver = new RotaryDriver();
            rotaryDriver.update(entity);
            rotaryDriver.setiHenshin();
            if (type == 0)
                rotaryDriver.handleRoundStart();
            if (type==1)
                rotaryDriver.handleRoundStop();
        }
    }
}
