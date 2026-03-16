package cheng.build.init;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class EntityAttributes {
    public static final AttributeSupplier
            build_up = PathfinderMob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 16.0D)
            .build(),
            effect_entity = PathfinderMob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 16.0D)
            .build();
}
