package cheng.build.model;

import cheng.build.GeoModelPath;
import cheng.build.bottle.InorganicMatterBottle;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class InorganicMatterBottleModel<T extends InorganicMatterBottle> extends AnimatedGeoModel<T> {
    private final ResourceLocation texture;

    public InorganicMatterBottleModel(ResourceLocation texture) {
        this.texture = texture;
    }

    @Override
    public ResourceLocation getModelLocation(T object) {
        return GeoModelPath.InorganicMatter(new ResourceLocation("")).model();
    }

    @Override
    public ResourceLocation getTextureLocation(T object) {
        return this.texture;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(T animatable) {
        return GeoModelPath.InorganicMatter(new ResourceLocation("")).animation();
    }
}
