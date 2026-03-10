package cheng.build.bottle;

import cheng.build.bottle.bottle_effect.BottleMobEffect;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public abstract class InorganicMatterBottle extends FullBottle{
    public InorganicMatterBottle(Properties pProperties, Supplier<BottleMobEffect> effect, Supplier<SoundEvent> soundevent) {
        super(pProperties, effect,soundevent);
    }
}
