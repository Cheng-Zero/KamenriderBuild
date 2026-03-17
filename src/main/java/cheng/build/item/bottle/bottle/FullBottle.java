package cheng.build.item.bottle.bottle;

import cheng.build.SoundUtil;
import cheng.build.api.IFullBottle;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public abstract class FullBottle extends Bottle {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public FullBottle(Properties pProperties, BottleType fullBottleType) {
        super(pProperties,fullBottleType);
    }

    @Override
    public void registerControllers(AnimationData data) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    /// 添加药水效果
    public void apply(IFullBottle iFullBottle, Player player, int time) {
        int t = time / 20;
        if (player.level.isClientSide()) return;
        if (t <= 30) {
            player.addEffect(new MobEffectInstance(iFullBottle.BottleMobEffect(), time, 0, true, false));
        } else
            player.addEffect(new MobEffectInstance(iFullBottle.BottleMobEffect(), 600, 0, true, false));
    }

    /// 播放音频
    public void playsound(IFullBottle iFullBottle,Player player){
        SoundUtil.playSound(player.level,(Entity) player,iFullBottle.sound(), SoundSource.PLAYERS);
    }
}
