package cheng.build.model;

import cheng.build.GeoModelPath;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BottleModel<T extends IAnimatable> extends AnimatedGeoModel<T> {
    private final ResourceLocation model;
    private final ResourceLocation texture;
    private final ResourceLocation animation;

    public BottleModel(ResourceLocation model, ResourceLocation texture, ResourceLocation animation) {
        this.model = model;
        this.texture = texture;
        this.animation = animation;
    }
    public BottleModel(ResourceLocation texture){
        this.model = GeoModelPath.empty_bottle.model();
        this.texture = texture;
        this.animation = GeoModelPath.empty_bottle.animation();
    }

    @Override
    public ResourceLocation getModelLocation(T object) {
        return this.model;
    }

    @Override
    public ResourceLocation getTextureLocation(T object) {
        return this.texture;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(T animatable) {
        return this.animation;
    }
}
