package cheng.build.entity;

import cheng.build.GeoModelPath;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class EffectEntityRenderer extends GeoEntityRenderer<EffectEntity> {
    public EffectEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new Model());
    }
    public static class Model extends AnimatedGeoModel<EffectEntity>{
        @Override
        public ResourceLocation getModelLocation(EffectEntity object) {
            if (object.getModel() != null)
                return object.getModel();
            else
                return GeoModelPath.BuildRabbatArmor.model();
        }

        @Override
        public ResourceLocation getTextureLocation(EffectEntity object) {
            if (object.getTexture() != null)
                return object.getTexture();
            else
                return GeoModelPath.BuildRabbatArmor.texture();
        }

        @Override
        public ResourceLocation getAnimationFileLocation(EffectEntity animatable) {
            if (animatable.getAnimations() != null)
                return animatable.getAnimations();
            else
                return GeoModelPath.BuildRabbatArmor.animation();
        }
    }
}
