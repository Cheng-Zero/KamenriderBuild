package cheng.build.entity;

import cheng.build.Build;
import cheng.build.GeoModelPath;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.renderers.geo.layer.LayerGlowingAreasGeo;
import software.bernie.geckolib3.util.EModelRenderCycle;

import java.util.Arrays;
import java.util.function.Function;

import static software.bernie.geckolib3.util.AnimationUtils.getGeoModelForEntity;
import static software.bernie.geckolib3.util.AnimationUtils.getRenderer;

public class BuildUpEffectRenderer extends GeoEntityRenderer<BuildUpEffectEntity> {
	public BuildUpEffectRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new BuildUpEffectEntityModel());
//		addLayer(new L(this));
	}

	private void renderStack(PoseStack poseStack){
		poseStack.translate(0, 1.5, 0);
		poseStack.scale(-1, -1, 1);
	}

	@Override
	public void render(BuildUpEffectEntity animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);

	}

	@Override
	public void render(GeoModel model, BuildUpEffectEntity animatable, float partialTick, RenderType type, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		RenderType cameo = RenderType.entityTranslucent(new ResourceLocation(Build.MODID,"textures/entity/build_up_effect_entity_e.png"));
		poseStack.pushPose();
		poseStack.scale(1.0f, 1.0f, 1.0f);
		poseStack.translate(0.0d, 0.0d, 0.0d);
		super.render(this.getGeoModelProvider().getModel(GeoModelPath.build_up.model()),
				animatable, partialTick, cameo, poseStack, bufferSource, bufferSource.getBuffer(cameo), packedLight,
				OverlayTexture.NO_OVERLAY, 1, 1, 1, alpha);
		poseStack.popPose();
	}

	private static class BuildUpEffectEntityModel extends AnimatedTickingGeoModel<BuildUpEffectEntity> {
		@Override
		public ResourceLocation getModelLocation(BuildUpEffectEntity object) {
			return GeoModelPath.build_up.model();
		}

		@Override
		public ResourceLocation getTextureLocation(BuildUpEffectEntity object) {
			return GeoModelPath.build_up.texture();
		}

		@Override
		public ResourceLocation getAnimationFileLocation(BuildUpEffectEntity animatable) {
			return GeoModelPath.build_up.animation();
		}
	}
	private static class L extends GeoLayerRenderer<BuildUpEffectEntity> {
		public L(IGeoRenderer<BuildUpEffectEntity> entityRendererIn) {
			super(entityRendererIn);
		}

		@Override
		public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, BuildUpEffectEntity entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

		}
	}
}