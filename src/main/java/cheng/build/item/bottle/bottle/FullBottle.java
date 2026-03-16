package cheng.build.item.bottle.bottle;

import cheng.build.api.IFullBottle;
import cheng.build.item.bottle.bottle_effect.BottleMobEffect;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.function.Supplier;

public abstract class FullBottle extends Bottle implements IFullBottle {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private final Supplier<BottleMobEffect> effect;
    private final Supplier<SoundEvent> soundEvent;

    public FullBottle(Properties pProperties, BottleType fullBottleType , Supplier<BottleMobEffect> effect, Supplier<SoundEvent> soundEvent) {
        super(pProperties,fullBottleType);
        this.effect = effect;
        this.soundEvent = soundEvent;
    }

    @Override
    public void registerControllers(AnimationData data) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public Supplier<SoundEvent> getSoundEvent() {
        return soundEvent;
    }

    @Override
    public BottleMobEffect BottleMobEffect() {
        return effect.get();
    }
}
