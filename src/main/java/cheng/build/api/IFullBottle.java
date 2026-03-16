package cheng.build.api;

import cheng.build.item.bottle.bottle_effect.BottleMobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

public interface IFullBottle extends IBottle{
    BottleMobEffect BottleMobEffect();

    default void apply(Player player, int time) {
        int t = time / 20;
        if (player.level.isClientSide()) return;
        if (t <= 30) {
            player.addEffect(new MobEffectInstance(BottleMobEffect(), time, 0, true, false));
        } else
            player.addEffect(new MobEffectInstance(BottleMobEffect(), 600, 0, true, false));
    }
}
