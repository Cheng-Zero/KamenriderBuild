package cheng.build.bottle.bottle_item;

import cheng.build.GeoModelPath;
import cheng.build.bottle.OrganicMatterBottle;
import cheng.build.init.InitMobEffect;
import cheng.build.init.InitSound;
import cheng.build.model.OrganicMatterBottleModel;
import cheng.build.render.BottleRenderer;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class Rabbat extends OrganicMatterBottle {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public Rabbat() {
        super(new Properties().stacksTo(1), InitMobEffect.rabbat_effect, InitSound.rabbat);
    }

    @Override
    public BottleRenderer<Rabbat> renderer() {
        return new BottleRenderer<>(new OrganicMatterBottleModel<>(GeoModelPath.RabbatBottleTexture));
    }

    @Override
    public void registerControllers(AnimationData data) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
