package cheng.build.network;

import cheng.build.data.DataManager;
import cheng.build.data.PlayerBuildData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PlayerBuildDataSyncPacket {

    private final UUID playerUUID;
    private final double hazardLevel;
    private final String mode;

    public PlayerBuildDataSyncPacket(PlayerBuildData data) {
        this.playerUUID = data.getPlayer().getUUID();
        this.hazardLevel = data.getHazardLevel();
        this.mode = data.getCurrentMode().name();
    }

    public PlayerBuildDataSyncPacket(FriendlyByteBuf buf) {
        this.playerUUID = buf.readUUID();
        this.hazardLevel = buf.readDouble();
        this.mode = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(playerUUID);
        buf.writeDouble(hazardLevel);
        buf.writeUtf(mode);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            if (context.getDirection().getReceptionSide().isClient()) {
                var mc = net.minecraft.client.Minecraft.getInstance();
                if (mc.player != null && mc.player.getUUID() == playerUUID) {
                    // 更新客户端缓存
                    PlayerBuildData data = DataManager.get(mc.player);
                    data.setHazardLevel(hazardLevel);
                    data.setCurrentMode(PlayerBuildData.TransformMode.valueOf(mode));
                }
            }
        });
        context.setPacketHandled(true);
    }
}
