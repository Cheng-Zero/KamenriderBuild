package cheng.build.keybingds;

import cheng.build.ItemHelper;
import cheng.build.armor.BuildDriver;
import cheng.build.bottle.ClearDriver;
import cheng.build.init.InitItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

import static cheng.build.armor.BuildDriver.*;

public class ClearDriverKeybingd {
    private final int type;
    private final int pressedms;

    public ClearDriverKeybingd(int type, int pressedms) {
        this.type = type;
        this.pressedms = pressedms;
    }

    public ClearDriverKeybingd(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
        this.pressedms = buffer.readInt();
    }

    public static void buffer(ClearDriverKeybingd message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
        buffer.writeInt(message.pressedms);
    }

    public static void handler(ClearDriverKeybingd message, Supplier<NetworkEvent.Context> contextSupplier) {
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
            ClearDriver clearDriver = new ClearDriver();
            // 数据更新
            clearDriver.updateItems(entity);
            if (type == 0)
                clearDriver.clear();
        }
    }
}
