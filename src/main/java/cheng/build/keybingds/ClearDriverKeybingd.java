package cheng.build.keybingds;

import cheng.build.program.ClearKeyProgram.ClearDriver;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class ClearDriverKeybingd extends ModKeybindings {
    public ClearDriverKeybingd(int type, int pressedms) {
        super(type, pressedms);
    }

    public ClearDriverKeybingd(FriendlyByteBuf buffer) {
        super(buffer);
    }

    // 直接调用父类的encode方法
    public static void encode(ClearDriverKeybingd message, FriendlyByteBuf buffer) {
        message.encode(buffer);  // 调用父类的encode
    }

    public static void handler(ClearDriverKeybingd message, Supplier<NetworkEvent.Context> contextSupplier) {
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
            ClearDriver clearDriver = new ClearDriver();
            // 数据更新
            clearDriver.update(entity);
            if (type == 0)
                clearDriver.clear();
        }
    }
}
