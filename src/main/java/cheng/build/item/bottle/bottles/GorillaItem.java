package cheng.build.item.bottle.bottles;

import cheng.build.GeoModelPath;
import cheng.build.item.bottle.bottle.OrganicMatterBottleItem;
import cheng.build.init.InitMobEffect;
import cheng.build.init.InitSound;
import cheng.build.client.model.BottleModel;
import cheng.build.client.renderer.BottleRenderer;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class GorillaItem extends OrganicMatterBottleItem {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public GorillaItem() {
        super(new Properties().stacksTo(1), InitMobEffect.rabbat_effect, InitSound.rabbat);
    }

    @Override
    public BottleRenderer<GorillaItem> renderer() {
        return new BottleRenderer<>(new BottleModel<>(GeoModelPath.GorillaBottle.texture()));
    }

    @Override
    public void registerControllers(AnimationData data) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public String getName() {
        return "Gorilla";
    }
}
