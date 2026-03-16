package cheng.build.item.bottle.bottles;

import cheng.build.GeoModelPath;
import cheng.build.item.bottle.bottle.Bottle;
import cheng.build.client.model.BottleModel;
import cheng.build.client.renderer.BottleRenderer;

public class EmptyBottleItem extends Bottle {
    public EmptyBottleItem(){
        this(new Properties().stacksTo(1));
    }
    public EmptyBottleItem(Properties properties) {
        super(properties, BottleType.EMPTY_MATTER_BOTTLE);
    }

    @Override
    public BottleRenderer<EmptyBottleItem> renderer() {
        return new BottleRenderer<>(new BottleModel<>(GeoModelPath.empty_bottle.texture()));
    }
}
