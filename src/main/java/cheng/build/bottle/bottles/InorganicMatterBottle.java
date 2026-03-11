package cheng.build.bottle.bottles;

import cheng.build.bottle.FullBottle;
import cheng.build.bottle.bottle_effect.BottleMobEffect;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public abstract class InorganicMatterBottle extends FullBottle {
    public InorganicMatterBottle(Properties pProperties, Supplier<BottleMobEffect> effect, Supplier<SoundEvent> soundevent) {
        super(pProperties,BottleType.INORGANIC_MATTER_BOTTLE ,effect,soundevent);
    }
}
