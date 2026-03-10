package cheng.build.bottle.bottle_effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class TankMobEffect extends BottleMobEffect {
    final String ARMOR = UUID.nameUUIDFromBytes("tankmobeffectarmor".getBytes(StandardCharsets.UTF_8)).toString();
    final String ARMOR_TOUGHNESS = UUID.nameUUIDFromBytes("tankmobeffectarmortoughness".getBytes(StandardCharsets.UTF_8)).toString();
    public TankMobEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        this.addAttributeModifier(Attributes.ARMOR, ARMOR,5, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(Attributes.ARMOR_TOUGHNESS, ARMOR_TOUGHNESS,3, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void addAttributeModifier(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
    }
}
