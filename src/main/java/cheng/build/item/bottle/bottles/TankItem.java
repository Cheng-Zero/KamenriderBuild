package cheng.build.item.bottle.bottles;

import cheng.build.GeoModelPath;
import cheng.build.init.InitItem;
import cheng.build.item.armor.base.BuildArmor;
import cheng.build.item.bottle.bottle.FullBottle;
import cheng.build.item.bottle.bottle.InorganicMatterBottleItem;
import cheng.build.init.InitMobEffect;
import cheng.build.init.InitSound;
import cheng.build.client.model.BottleModel;
import cheng.build.client.renderer.BottleRenderer;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.function.Supplier;

public class TankItem extends InorganicMatterBottleItem {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public TankItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public BottleRenderer<TankItem> renderer() {
        return new BottleRenderer<>(new BottleModel<>(GeoModelPath.TankBottle.texture()));
    }

    @Override
    public void registerControllers(AnimationData data) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
