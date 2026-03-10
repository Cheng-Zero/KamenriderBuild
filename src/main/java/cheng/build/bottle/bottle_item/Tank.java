package cheng.build.bottle.bottle_item;

import cheng.build.GeoModelPath;
import cheng.build.bottle.InorganicMatterBottle;
import cheng.build.init.InitMobEffect;
import cheng.build.init.InitSound;
import cheng.build.model.InorganicMatterBottleModel;
import cheng.build.render.BottleRenderer;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class Tank extends InorganicMatterBottle {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public Tank() {
        super(new Properties().stacksTo(1), InitMobEffect.tank_effect, InitSound.tank);
    }

    @Override
    public BottleRenderer<Tank> renderer() {
        return new BottleRenderer<>(new InorganicMatterBottleModel<>(GeoModelPath.TankBottleTexture));
    }

    @Override
    public void registerControllers(AnimationData data) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
