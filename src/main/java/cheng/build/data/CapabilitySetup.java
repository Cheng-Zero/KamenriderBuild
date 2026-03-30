package cheng.build.data;

import cheng.build.Build;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Build.MODID)
public class CapabilitySetup {
    
    // 注册 Capability 类型
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IBuildCapability.class);
    }
    
    // 附加 Capability 到玩家实体
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            event.addCapability(
                new ResourceLocation(Build.MODID, "player_data"),
                new PlayerDataProvider(player)
            );
        }
    }
}