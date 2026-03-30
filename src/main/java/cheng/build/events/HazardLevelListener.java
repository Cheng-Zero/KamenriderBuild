package cheng.build.events;

import cheng.build.data.DataManager;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class HazardLevelListener {
    @SubscribeEvent
    public static void HazardFall(LivingFallEvent event){
        Fall(event);
    }
    @SubscribeEvent
    public static void HazardHeal(LivingHealEvent event){
        Heal(event);
    }
    @SubscribeEvent
    public static void HazardLevelAttact(LivingHurtEvent event) {
        AttactAndDamageReduction(event);
    }
    @SubscribeEvent
    public static void HazardKnockBack(LivingKnockBackEvent event){
        KnockBack(event);
    }
    /**
     * 拥有危险等级的实体恢复时增加回血量(单次恢复大于10不生效)
     */
    private static void Heal(LivingHealEvent event){
        LivingEntity entity = event.getEntityLiving();  // 摔落的实体
        float amount = event.getAmount(); // 治疗量

        if (entity instanceof Player player) {
            float hazardLevel = (float) DataManager.get(player).hazard_level;

            float finalHeal = hazardLevel + amount;
            if (finalHeal <= 10){
                event.setAmount(finalHeal);
            }
        }
    }
    /**
     * 拥有危险等级的实体摔落时减伤和增加摔伤高度
     */
    private static void Fall(LivingFallEvent event){
        LivingEntity entity = event.getEntityLiving();  // 摔落的实体
        float distance = event.getDistance();           // 摔落距离（格数）

        if (entity instanceof Player player) {
            float hazardLevel = (float) DataManager.get(player).hazard_level;

            float finalDistance = distance - (hazardLevel);
            // 修改摔落距离
            event.setDistance(finalDistance);
//            Build.LOGGER.debug("掉落距离{},已减少{}格摔落高度",distance,finalDistance-distance);
        }
    }
    /**
     * 拥有危险等级的实体攻击造成伤害和受到伤害减伤
     */
    private static void AttactAndDamageReduction (LivingHurtEvent event){
        // 伤害
        DamageSource source = event.getSource();
        // 伤害值
        float amount = event.getAmount();
        // 攻击实体
        Entity entity = source.getEntity();
        // 被攻击实体
        LivingEntity entityLiving = event.getEntityLiving();
        float hazardLevel;

        if (entity == null || entityLiving == null) return;

        // 如果攻击实体是玩家
        if (entity instanceof Player player){
            // 获取危险等级
            hazardLevel = (float) DataManager.get(player).hazard_level;
            float finalAmount = amount * hazardLevel;
            // 设置返回伤害
            event.setAmount(finalAmount);
//            Build.LOGGER.debug("最终伤害值：{}", finalAmount);
        }
        // 如果被攻击实体是玩家
        else if (entityLiving instanceof Player player){
            hazardLevel = (float) DataManager.get(player).hazard_level;
            float finalAmount = ((100 - hazardLevel) / 100) * amount;
            event.setAmount(finalAmount);
//            Build.LOGGER.debug("最终受伤值：{}", finalAmount);
        }
    }
    /**
     * 拥有危险等级的实体击退抗性
     */
    private static void KnockBack(LivingKnockBackEvent event){
        LivingEntity entityLiving = event.getEntityLiving();
        float strength = event.getStrength();
        if (entityLiving instanceof Player player) {
            float hazardLevel = (float) DataManager.get(player).hazard_level;

            float finalAmount = ((100 - hazardLevel) / 100) * strength;
            // 修改摔落距离
            event.setStrength(finalAmount);
        }
    }
}
