package cheng.build.bottle;

import cheng.build.model.EmptyBottleModel;
import cheng.build.render.BottleRenderer;

public class EmptyBottle extends Bottle{
    public EmptyBottle() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public BottleRenderer<EmptyBottle> renderer() {
        return new BottleRenderer<>(new EmptyBottleModel());
    }
}
