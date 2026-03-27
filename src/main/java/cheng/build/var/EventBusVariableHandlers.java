package cheng.build.var;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.Set;

import static cheng.build.var.ModVariables.PLAYER_VARIABLES_CAPABILITY;

@Mod.EventBusSubscriber
public class EventBusVariableHandlers {
	@SubscribeEvent
	public static void onPlayerLoggedInSyncPlayerVariables(PlayerEvent.PlayerLoggedInEvent event) {
		if (!event.getEntity().level.isClientSide())
			sync(event);
	}

	@SubscribeEvent
	public static void onPlayerRespawnedSyncPlayerVariables(PlayerEvent.PlayerRespawnEvent event) {
		if (!event.getEntity().level.isClientSide())
			sync(event);
	}

	@SubscribeEvent
	public static void onPlayerChangedDimensionSyncPlayerVariables(PlayerEvent.PlayerChangedDimensionEvent event) {
		if (!event.getEntity().level.isClientSide())
			sync(event);
	}

	@SubscribeEvent
	public static void clonePlayer(PlayerEvent.Clone event) {
		Player player = event.getOriginal();
		Entity entity = event.getEntity();
		player.revive();
		PlayerVariables original = player.getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables());
		PlayerVariables clone = entity.getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables());
		if (!event.isWasDeath()) {}
		clone.hazard_level = original.hazard_level;
	}

	/**
	 * 同步变量
	 */
	private static void sync(PlayerEvent event){
		event.getEntity().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()).syncPlayerVariables(event.getEntity());
	}
}