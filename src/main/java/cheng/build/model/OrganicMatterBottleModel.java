package cheng.build.model;

import cheng.build.GeoModelPath;
import cheng.build.bottle.OrganicMatterBottle;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class OrganicMatterBottleModel<T extends OrganicMatterBottle> extends AnimatedGeoModel<T> {
    private final ResourceLocation texture;

    public OrganicMatterBottleModel(ResourceLocation texture) {
        this.texture = texture;
    }

    @Override
    public ResourceLocation getModelLocation(T object) {
        return GeoModelPath.OrganicMatter(new ResourceLocation("")).model();
    }

    @Override
    public ResourceLocation getTextureLocation(T object) {
        return this.texture;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(T animatable) {
        return GeoModelPath.OrganicMatter(new ResourceLocation("")).animation();
    }
}
