package cheng.build.item.bottle.bottle_effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public abstract class BottleMobEffect extends MobEffect {
    protected BottleMobEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }
    @Override
    public void addAttributeModifiers(LivingEntity living, AttributeMap attributeMap, int amplifier) {
        addAttributeModifier(living, attributeMap, amplifier);
        super.addAttributeModifiers(living,attributeMap,amplifier);
    }
    /// 开始时
    public abstract void addAttributeModifier(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier);
    /// 结束时
//    public abstract void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier);
}
