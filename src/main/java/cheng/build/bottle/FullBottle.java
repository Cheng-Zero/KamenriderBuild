package cheng.build.bottle;

import cheng.build.bottle.bottle_effect.BottleMobEffect;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.function.Supplier;

public abstract class FullBottle extends Bottle{
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private final Supplier<BottleMobEffect> effect;
    private final Supplier<SoundEvent> soundEvent;

    public FullBottle(Properties pProperties, Supplier<BottleMobEffect> effect,Supplier<SoundEvent> soundEvent) {
        super(pProperties);
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

    public MobEffect getEffect() {
        return effect.get();
    }
    // 获取使用时间 是否小于等于30秒
    // 是给予对应秒的buff
    // 超过30秒 给予30秒buff
    public void applyEffect(Player player, int time){
        int t = time/20;
        if (player.level.isClientSide())return;
        if (t <= 30) {
            player.addEffect(new MobEffectInstance(getEffect(), time, 0, true, false));
        } else
            player.addEffect(new MobEffectInstance(getEffect(),600,0,true,false));
    }

    public Supplier<SoundEvent> getSoundEvent() {
        return soundEvent;
    }
}
