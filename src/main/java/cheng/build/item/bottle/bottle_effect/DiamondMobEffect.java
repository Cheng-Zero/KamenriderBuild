package cheng.build.item.bottle.bottle_effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class DiamondMobEffect extends BottleMobEffect {
    final String armor = UUID.nameUUIDFromBytes((this.getClass().getName()+"_armor").getBytes(StandardCharsets.UTF_8)).toString();
    final String knockback_resistance = UUID.nameUUIDFromBytes((this.getClass().getName()+"_KNOCKBACK_RESISTANCE").getBytes(StandardCharsets.UTF_8)).toString();
    public DiamondMobEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        this.addAttributeModifier(Attributes.ARMOR, armor,5.5, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, knockback_resistance,0.1, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void addAttributeModifier(LivingEntity living, AttributeMap attributeMap, int amplifier) {
    }
}
