package cheng.build.item.bottle.bottle_effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class GorillaMobEffect extends BottleMobEffect {
    final String speed = UUID.nameUUIDFromBytes("rabbatmobeffect_speed".getBytes(StandardCharsets.UTF_8)).toString();
    final String jump = UUID.nameUUIDFromBytes("rabbatmobeffectjumpstrength".getBytes(StandardCharsets.UTF_8)).toString();
    public GorillaMobEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, speed,1, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(Attributes.JUMP_STRENGTH, jump,100, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void addAttributeModifier(LivingEntity living, AttributeMap attributeMap, int amplifier) {
    }
}
