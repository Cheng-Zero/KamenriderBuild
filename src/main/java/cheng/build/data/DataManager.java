package cheng.build.data;

import net.minecraft.world.entity.player.Player;

public class DataManager {
    
    // 获取玩家数据（一行代码搞定）
    public static PlayerBuildData get(Player player) {
        return player.getCapability(PlayerDataProvider.PLAYER_DATA)
                .orElse(new PlayerDataImpl(player))
                .getData();
    }
}