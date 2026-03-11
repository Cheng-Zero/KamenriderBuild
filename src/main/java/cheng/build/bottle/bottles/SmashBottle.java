package cheng.build.bottle.bottles;

import cheng.build.GeoModelPath;
import cheng.build.model.BottleModel;
import cheng.build.render.BottleRenderer;

public class SmashBottle extends Bottle{
    public SmashBottle() {
        this(new Properties().stacksTo(16));
    }
    public SmashBottle(Properties properties) {
        super(properties, BottleType.SMASH_BOTTLE);
    }

    @Override
    public BottleRenderer renderer() {
        return new BottleRenderer<>(new BottleModel<>(
                GeoModelPath.smash_bottle.model(),
                GeoModelPath.smash_bottle.texture(),
                GeoModelPath.smash_bottle.animation()
        ));
    }
}
