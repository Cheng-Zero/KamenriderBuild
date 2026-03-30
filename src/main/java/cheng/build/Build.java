package cheng.build;

import cheng.build.keybingds.GetHolderKeybingd;
import cheng.build.network.PlayerBuildDataSyncPacket;
import cheng.build.player_animation.SetupAnimationsProcedure;
import cheng.build.init.*;
import cheng.build.keybingds.ClearDriverKeybingd;
import cheng.build.keybingds.ShakeBottleMessageKey;
import cheng.build.keybingds.RotaryDriverMessageKey;
import com.mojang.logging.LogUtils;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;
import software.bernie.geckolib3.GeckoLib;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Build.MODID)
public class Build {

    public static final String MODID= "kamenrider_build";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Build() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        bus.addListener(this::registerMessage);
        bus.addListener(this::onClientSetup);

        bus.addListener(this::registerEntityAttributes);

        init(bus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        forgeBus.register(this);
        GeckoLib.initialize();
    }

    private static final String PROTOCOL_VERSION = "1";
    private static int messageID = 0;
    public static final SimpleChannel PACKET_HANDLER =
            NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, MODID),
                    () -> PROTOCOL_VERSION,
                    PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    /**
     *
     * @param messageType 网络类型
     * @param encoder 载入缓存
     * @param decoder 读取缓存
     * @param messageConsumer 消息处理
     * @param <T> 目标类
     */
    public <T> void addNetworkMessage(Class<T> messageType,
                                             BiConsumer<T, FriendlyByteBuf> encoder,
                                             Function<FriendlyByteBuf, T> decoder,
                                             BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        PACKET_HANDLER.registerMessage(messageID,
                messageType,
                encoder,
                decoder,
                messageConsumer);
        ++messageID;
    }

    private void registerMessage(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // 确保在网络线程中注册
            addNetworkMessage(ShakeBottleMessageKey.class, ShakeBottleMessageKey::buffer, ShakeBottleMessageKey::new, ShakeBottleMessageKey::handler);
            addNetworkMessage(RotaryDriverMessageKey.class, RotaryDriverMessageKey::buffer, RotaryDriverMessageKey::new, RotaryDriverMessageKey::handler);
            addNetworkMessage(ClearDriverKeybingd.class, ClearDriverKeybingd::encode, ClearDriverKeybingd::new, ClearDriverKeybingd::handler);
            addNetworkMessage(GetHolderKeybingd.class, GetHolderKeybingd::buffer, GetHolderKeybingd::new, GetHolderKeybingd::handler);
            addNetworkMessage(PlayerBuildDataSyncPacket.class, PlayerBuildDataSyncPacket::encode, PlayerBuildDataSyncPacket::new, PlayerBuildDataSyncPacket::handle);

            LOGGER.info("Registered network messages for " + MODID);
        });
    }

    private void registerEntityAttributes(EntityAttributeCreationEvent event) {
        InitEntity.Attributes_FOR_DATAGEN.forEach((e,a)->
                event.put(e.get(),a.get()));
    }
    /// 使用 PlayerAnimationAccess 注册动画工厂
    private void onClientSetup(FMLClientSetupEvent event) {
        PlayerAnimationAccess.REGISTER_ANIMATION_EVENT.register(SetupAnimationsProcedure::registerPlayerAnimation);
    }
    private void init(IEventBus bus){
        InitSound.SOUNDS.register(bus);
        InitItem.register.register(bus);
        InitBlock.register.register(bus);
        InitModBlockEntities.REGISTRY.register(bus);
        InitEntity.registry.register(bus);
        InitMobEffect.register.register(bus);

        InitTab.load();
    }
}
