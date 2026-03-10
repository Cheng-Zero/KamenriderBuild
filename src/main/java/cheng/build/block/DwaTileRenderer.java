package cheng.build.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class DwaTileRenderer extends GeoBlockRenderer<FullbottlePurifierEntity> {
	public DwaTileRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
		super(rendererDispatcherIn, new DwaBlockModel());
	}

	@Override
	public void render(FullbottlePurifierEntity animation, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		super.render(animation, partialTick, poseStack, bufferSource, packedLight);
		ItemStack stackInSlot = animation.getInventory().getStackInSlot(0);
		ItemRenderer item = Minecraft.getInstance().getItemRenderer();
		poseStack.pushPose();
		poseStack.scale(1f,1f,1f);
		poseStack.translate(0.5f,0.45f,0.55f);
		if (animation.getLevel() != null){
			item.renderStatic(stackInSlot, ItemTransforms.TransformType.FIXED,
					packedLight,1,poseStack,bufferSource,1);
		}
		poseStack.popPose();
	}

	@Override
	public RenderType getRenderType(FullbottlePurifierEntity animatable, float partialTick, PoseStack poseStack,
									MultiBufferSource bufferSource, VertexConsumer buffer,
									int packedLight, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));  // 关键！
	}
}
