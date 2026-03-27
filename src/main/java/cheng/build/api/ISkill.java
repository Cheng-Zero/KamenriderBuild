package cheng.build.api;

import net.minecraft.world.entity.LivingEntity;

public interface ISkill {
    /// 冷却
    int getCooldowns();
    /// 使用方法
    void skill(LivingEntity living);
    /// 使用条件
    boolean isAvailable(LivingEntity living);
}
