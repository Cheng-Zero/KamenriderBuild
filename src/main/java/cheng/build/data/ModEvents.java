package cheng.build.data;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ModEvents {
    // 玩家死亡重生时，复制数据
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player original = event.getOriginal();
        Player player = event.getPlayer();
        
        original.getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(old -> {
            player.getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(newData -> {
                if (event.isWasDeath()) {
                    newData.copyFrom(old);
                }
            });
        });
    }
}