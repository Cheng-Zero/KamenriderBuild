package cheng.build.player_animation;

import cheng.build.Build;
import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.IdentityHashMap;
import java.util.Map;

public class SetupAnimationsProcedure {
    public static final Map<AbstractClientPlayer, ModifierLayer<IAnimation>> animationData = new IdentityHashMap<>();

    public static void registerPlayerAnimation(AbstractClientPlayer player, AnimationStack stack) {
        ModifierLayer<IAnimation> layer = new ModifierLayer<>();
        stack.addAnimLayer(1000, layer);
        animationData.put(player, layer);
    }

    /**
     * 播放或停止玩家的动画
     * @param player 玩家
     * @param animationName 动画名称
     * @param start 是否开始播放
     */
    public static void playAnimation(Player player, String animationName, boolean start) {
        // start 为 true 时播放动画，false 时停止动画
        if (player instanceof AbstractClientPlayer clientPlayer) {
            // 从动画数据中获取玩家的动画层
            ModifierLayer<IAnimation> layer = animationData.get(clientPlayer);
            // 如果动画层存在
            if (layer != null) {
                if (start) {
                    // 从动画注册表中获取动画
                    ResourceLocation animId = new ResourceLocation(Build.MODID, animationName);
                    KeyframeAnimation animation = PlayerAnimationRegistry.getAnimation(animId);
                    if (animation != null) {
                        // 设置动画层的动画为 KeyframeAnimationPlayer
                        // KeyframeAnimationPlayer 是一个动画播放器，用于播放 KeyframeAnimation 动画
                        // 这里将获取到的动画封装为 KeyframeAnimationPlayer 并设置给动画层
                        layer.setAnimation(new KeyframeAnimationPlayer(animation));
                    }
                } else {
                    // 如果 start 为 false，设置动画层的动画为 null，停止动画
                    layer.setAnimation(null);
                }
            }
        }
    }

    public static void stopAnimation(Player player) {
        playAnimation(player, "", false); // 调用 playAnimation 方法停止动画
    }

    // 添加获取动画层的方法
    public static ModifierLayer<IAnimation> getAnimationLayer(Player player) {
        if (player instanceof AbstractClientPlayer clientPlayer) {
            return animationData.get(clientPlayer);
        }
        return null;
    }
}