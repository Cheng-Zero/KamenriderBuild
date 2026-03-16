package cheng.build.client.model;

import cheng.build.item.armor.base.ARMOR;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ARMORModel<T extends ARMOR> extends AnimatedGeoModel<T> {
    private final ResourceLocation model;
    private final ResourceLocation texture;
    private final ResourceLocation animation;

    public ARMORModel(ResourceLocation model, ResourceLocation texture, ResourceLocation animation) {
        this.model = model;
        this.texture = texture;
        this.animation = animation;
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
