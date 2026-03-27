package cheng.build.rider_syteam;

import cheng.build.api.ISkill;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class SkillManager {
    
    private final Map<String, ISkill> skills = new HashMap<>();
    
    // 注册技能
    public void registerSkill(String name, ISkill skill) {
        skills.put(name, skill);
    }
    
    // 执行技能
    public boolean useSkill(String name, LivingEntity user) {
        ISkill skill = skills.get(name);
        if (skill == null) {
            return false;
        }
        
        if (skill.isAvailable(user)) {
            skill.skill(user);
            return true;
        }
        return false;
    }
    
    // 获取技能
    public ISkill getSkill(String name) {
        return skills.get(name);
    }
    
    // 检查技能是否可用
    public boolean isSkillAvailable(String name, LivingEntity user) {
        ISkill skill = skills.get(name);
        return skill != null && skill.isAvailable(user);
    }
    
    // 获取冷却剩余时间（秒）
    public long getCooldownRemaining(String name, LivingEntity user) {
        // 这个需要扩展 BaseSkill 来暴露 lastUseTime
        // 简单实现可以返回 0
        return 0;
    }
}