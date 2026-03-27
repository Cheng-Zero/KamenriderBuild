package cheng.build.rider_syteam.skill;

import cheng.build.api.ISkill;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public abstract class BaseSkill implements ISkill {
    
    protected long lastUseTime = 0;
    protected int cooldownSeconds = 0;
    
    public BaseSkill(int cooldownSeconds) {
        this.cooldownSeconds = cooldownSeconds;
    }
    
    @Override
    public int getCooldowns() {
        return cooldownSeconds;
    }
    
    @Override
    public boolean isAvailable(LivingEntity living) {
        long currentTime = living.level.getGameTime();
        long elapsed = (currentTime - lastUseTime) / 20; // 转换为秒
        
        if (elapsed >= cooldownSeconds) {
            return true;
        }
        
        // 冷却中，发送提示
        if (living instanceof ServerPlayer player) {
            long remaining = cooldownSeconds - elapsed;
            player.displayClientMessage(
                new TextComponent("技能冷却中，还需 " + remaining + " 秒")
                    .withStyle(ChatFormatting.RED),
                true
            );
        }
        return false;
    }
    
    @Override
    public void skill(LivingEntity living) {
        if (isAvailable(living)) {
            lastUseTime = living.level.getGameTime();
            executeSkill(living);
        }
    }
    
    // 子类实现具体技能逻辑
    protected abstract void executeSkill(LivingEntity living);
}