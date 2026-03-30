package cheng.build.message;

import cheng.build.Build;
import cheng.build.network.PlayerBuildDataSyncPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

/**
 * @author 千云亦（YunYi-Qian）
 */
@Mod.EventBusSubscriber(modid = Build.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class NetworkHandler {
    private static final String PROTOCOL = "1";
    private static final ResourceLocation CHANNEL_ID =
            new ResourceLocation(Build.MODID, "main");

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            CHANNEL_ID, () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

    private static int id = 0;
    private static boolean initialized = false;

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent e) {
        if (initialized) return;
        initialized = true;

        e.enqueueWork(() -> {
            registerClientMessage(AnimationMessage.class);
        });
    }

    private static <T extends IMessage> void registerClientMessage(Class<T> messageType) {
        registerMessage(messageType, NetworkDirection.PLAY_TO_CLIENT);
    }

    private static <T extends IMessage> void registerServerMessage(Class<T> messageType) {
        registerMessage(messageType, NetworkDirection.PLAY_TO_SERVER);
    }

    private static <T extends IMessage> void registerMessage(Class<T> messageType, NetworkDirection direction) {
        CHANNEL.registerMessage(
                id++,
                messageType,
                IMessage::encode,
                buf -> {
                    try {
                        return messageType.getConstructor(FriendlyByteBuf.class).newInstance(buf);
                    } catch (Exception e) {
                        throw new RuntimeException("无法实例化消息 " + messageType.getSimpleName(), e);
                    }
                },
                (msg, ctx) -> {
                    ctx.get().enqueueWork(() -> msg.handle(ctx.get()));
                    ctx.get().setPacketHandled(true);
                },
                Optional.of(direction)
        );
    }

    /* ========== 消息发送方法 ========== */

    // 发送给服务端（包括发送者）
    public static void sendToServer(IMessage msg) {
        CHANNEL.sendToServer(msg);
    }

    // 发送给指定玩家（包括发送者）
    public static void sendTo(ServerPlayer player, IMessage msg) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    // 发送给指定玩家（包括发送者）
    public static void sendToPlayer(IMessage msg, ServerPlayer player) {
        sendTo(player, msg); // sendToPlayer是sendTo的别名
    }

    // 发送给所有玩家（包括发送者）
    public static void sendToAll(IMessage msg) {
        CHANNEL.send(PacketDistributor.ALL.noArg(), msg);
    }

    // 发送给指定维度的所有玩家
    public static void sendToDimension(ResourceKey<Level> dim, IMessage msg) {
        CHANNEL.send(PacketDistributor.DIMENSION.with(() -> dim), msg);
    }

    // 发送给跟踪实体（包括发送者）
    public static void sendToTracking(Entity entity, IMessage msg) {
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), msg);
    }

    // 发送给跟踪玩家（包括发送者）
    public static void sendToTrackingPlayers(IMessage msg, ServerPlayer player) {
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), msg);
    }
}