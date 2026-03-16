package cheng.build.item.bottle.bottles;

import cheng.build.GeoModelPath;
import cheng.build.item.bottle.bottle.InorganicMatterBottleItem;
import cheng.build.init.InitMobEffect;
import cheng.build.init.InitSound;
import cheng.build.client.model.BottleModel;
import cheng.build.client.renderer.BottleRenderer;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class DiamondItem extends InorganicMatterBottleItem {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public DiamondItem() {
        super(new Properties().stacksTo(1), InitMobEffect.tank_effect, InitSound.tank);
    }

    @Override
    public BottleRenderer<DiamondItem> renderer() {
        return new BottleRenderer<>(new BottleModel<>(GeoModelPath.DiamondBottle.texture()));
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
        return "Diamond";
    }
}
