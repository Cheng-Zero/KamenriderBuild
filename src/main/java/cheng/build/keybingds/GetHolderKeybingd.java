package cheng.build.keybingds;

import cheng.build.Build;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class GetHolderKeybingd extends ModKeybindings{
    public GetHolderKeybingd(int type, int pressedms) {
        super(type, pressedms);
    }

    public GetHolderKeybingd(FriendlyByteBuf buffer) {
        super(buffer);
    }

    public static void buffer(GetHolderKeybingd message, FriendlyByteBuf buffer) {
        message.encode(buffer);
    }

    public static void handler(GetHolderKeybingd message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () ->
                () -> pressAction(Objects.requireNonNull(context.getSender()), message.getType(), message.getPressedms())));
        context.setPacketHandled(true);
    }

    public static void pressAction(Player entity, int type, int pressedms) {
        Level world = entity.level;
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        if (world.hasChunkAt(entity.blockPosition())) {
            // 数据更新
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null)
                if (mc.player != null && mc.screen == null) {
                // 打开选择界面
                if (type == 0) {
//                    BuildDriverFullBottleHolder.getInstance().open();
                }
                Build.LOGGER.debug("按键打开GUI");
            }else {
                    mc.setScreen(null);
                }
        }
    }
}
