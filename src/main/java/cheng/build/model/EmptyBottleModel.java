package cheng.build.model;

import cheng.build.GeoModelPath;
import cheng.build.bottle.bottles.EmptyBottle;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class EmptyBottleModel extends AnimatedGeoModel<EmptyBottle> {
    @Override
    public ResourceLocation getModelLocation(EmptyBottle object) {
        return GeoModelPath.empty_bottle.model();
    }

    @Override
    public ResourceLocation getTextureLocation(EmptyBottle object) {
        return GeoModelPath.empty_bottle.texture();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EmptyBottle animatable) {
        return GeoModelPath.empty_bottle.animation();
    }
}
