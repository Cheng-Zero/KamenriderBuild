package cheng.build.rider_syteam.skill;

import cheng.build.api.ISkill;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public class RabbatJump implements ISkill {
    private long lastUseTime = 0;
    private static final int COOLDOWN_SECONDS = 3;  // 3秒冷却

    @Override
    public void skill(LivingEntity living) {
        if (!isAvailable(living)) return;

        // 记录使用时间
        lastUseTime = living.level.getGameTime();

        // 执行跳跃
        jump(living);
    }

    private void jump(LivingEntity living) {
        // 让实体跳跃
        living.setJumping(true);

        // 可选：添加额外效果
        if (living instanceof ServerPlayer player) {
            player.displayClientMessage(
                    new TextComponent("跳跃！").withStyle(ChatFormatting.GREEN),
                    true
            );
        }
    }

    @Override
    public boolean isAvailable(LivingEntity living) {
        long currentTime = living.level.getGameTime();
        long elapsed = (currentTime - lastUseTime) / 20;  // 转换为秒

        if (elapsed >= COOLDOWN_SECONDS) {
            return true;
        }

        // 冷却中
        if (living instanceof ServerPlayer player) {
            long remaining = COOLDOWN_SECONDS - elapsed;
            player.displayClientMessage(
                    new TextComponent("跳跃冷却中，还需 " + remaining + " 秒")
                            .withStyle(ChatFormatting.RED),
                    true
            );
        }
        return false;
    }

    @Override
    public int getCooldowns() {
        return COOLDOWN_SECONDS;
    }
}
