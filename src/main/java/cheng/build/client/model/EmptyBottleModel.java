package cheng.build.client.model;

import cheng.build.GeoModelPath;
import cheng.build.item.bottle.bottles.EmptyBottleItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class EmptyBottleModel extends AnimatedGeoModel<EmptyBottleItem> {
    @Override
    public ResourceLocation getModelLocation(EmptyBottleItem object) {
        return GeoModelPath.empty_bottle.model();
    }

    @Override
    public ResourceLocation getTextureLocation(EmptyBottleItem object) {
        return GeoModelPath.empty_bottle.texture();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EmptyBottleItem animatable) {
        return GeoModelPath.empty_bottle.animation();
    }
}
