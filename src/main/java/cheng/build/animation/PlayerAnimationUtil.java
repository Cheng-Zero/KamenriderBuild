package cheng.build.animation;

import cheng.build.Build;
import cheng.build.message.AnimationMessage;
import cheng.build.message.NetworkHandler;
import net.minecraft.server.level.ServerPlayer;

public class PlayerAnimationUtil {
    /**
     * 使用玩家动作 (包括网络传输)
     *
     * @param serverPlayer 服务端玩家实例
     * @param animationName 动画名称
     * @param start 是否使用
     */
    public static void playanimation(ServerPlayer serverPlayer, String animationName, boolean start) {
        Build.LOGGER.info("Broadcasting animation: {} for player: {}", animationName, serverPlayer.getName().getString());
        NetworkHandler.sendToTrackingPlayers(
                new AnimationMessage(animationName, serverPlayer.getUUID(), start),
                serverPlayer
        );
    }
}
