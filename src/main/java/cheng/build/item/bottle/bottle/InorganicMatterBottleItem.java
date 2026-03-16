package cheng.build.item.bottle.bottle;

import cheng.build.item.bottle.bottle_effect.BottleMobEffect;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public abstract class InorganicMatterBottleItem extends FullBottle {
    public InorganicMatterBottleItem(Properties pProperties, Supplier<BottleMobEffect> effect, Supplier<SoundEvent> soundevent) {
        super(pProperties,BottleType.INORGANIC_MATTER_BOTTLE ,effect,soundevent);
    }
}
