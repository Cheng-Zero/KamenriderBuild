package cheng.build.item.bottle.bottles;

import cheng.build.GeoModelPath;
import cheng.build.item.bottle.bottle.Bottle;
import cheng.build.client.model.BottleModel;
import cheng.build.client.renderer.BottleRenderer;

public class SmashBottleItem extends Bottle {
    public SmashBottleItem() {
        this(new Properties().stacksTo(16));
    }
    public SmashBottleItem(Properties properties) {
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
