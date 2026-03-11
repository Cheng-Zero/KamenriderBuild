package cheng.build.bottle.bottles;

import cheng.build.GeoModelPath;
import cheng.build.model.BottleModel;
import cheng.build.render.BottleRenderer;

public class EmptyBottle extends Bottle{
    public EmptyBottle(){
        this(new Properties().stacksTo(1));
    }
    public EmptyBottle(Properties properties) {
        super(properties, BottleType.EMPTY_MATTER_BOTTLE);
    }

    @Override
    public BottleRenderer<EmptyBottle> renderer() {
        return new BottleRenderer<>(new BottleModel<>(GeoModelPath.empty_bottle.texture()));
    }
}
