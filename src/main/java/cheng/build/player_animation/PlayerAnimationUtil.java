package cheng.build.player_animation;

import cheng.build.Build;
import cheng.build.message.AnimationMessage;
import cheng.build.message.NetworkHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PlayerAnimationUtil {
    /**
     * 使用玩家动作 (包括网络传输)
     *
     * @param player 玩家实例
     * @param animationName 动画名称
     * @param start 是否使用
     */
    public static void playanimation(Player player, String animationName, boolean start) {
        Build.LOGGER.info("玩家：{} 使用动画：{}", player.getName().getString(),animationName);
        if (player instanceof ServerPlayer serverPlayer)
         NetworkHandler.sendToTrackingPlayers(
                new AnimationMessage(animationName, serverPlayer.getUUID(), start),
                serverPlayer
        );
    }
}
