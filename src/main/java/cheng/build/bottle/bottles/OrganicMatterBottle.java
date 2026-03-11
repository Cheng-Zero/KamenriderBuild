package cheng.build.bottle.bottles;

import cheng.build.bottle.FullBottle;
import cheng.build.bottle.bottle_effect.BottleMobEffect;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public abstract class OrganicMatterBottle extends FullBottle {
    public OrganicMatterBottle(Properties pProperties, Supplier<BottleMobEffect> effect, Supplier<SoundEvent> soundevent) {
        super(pProperties, BottleType.ORGANIC_MATTER_BOTTLE,effect,soundevent);
    }
}
