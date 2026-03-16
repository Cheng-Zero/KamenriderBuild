package cheng.build.block.renderer;

import cheng.build.block.model.FullbottlePurifierBlockModel;
import cheng.build.block.entity.FullbottlePurifierBlockEntity;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class FullbottlePurifierBlockRenderer extends GeoBlockRenderer<FullbottlePurifierBlockEntity> {
	public FullbottlePurifierBlockRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
		super(rendererDispatcherIn, new FullbottlePurifierBlockModel());
	}

	@Override
	public void render(FullbottlePurifierBlockEntity animation, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		super.render(animation, partialTick, poseStack, bufferSource, packedLight);
		ItemStack stackInSlot = animation.getInventory().getStackInSlot(0);
		ItemRenderer item = Minecraft.getInstance().getItemRenderer();
		BlockState blockState = animation.getBlockState();
		Direction facing = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);

		poseStack.pushPose();
		poseStack.scale(1f,1f,1f);
//		poseStack.translate(0.5f,0.45f,0.55f);
		positionByFacing(poseStack,facing);
		rotateItemByFacing(poseStack,facing);

		if (animation.getLevel() != null){
			item.renderStatic(stackInSlot, ItemTransforms.TransformType.FIXED,
					packedLight,1,poseStack,bufferSource,1);
		}
		poseStack.popPose();
	}
	/**
	 * 根据方块朝向旋转物品
	 */
	private void rotateItemByFacing(PoseStack poseStack, Direction facing) {
		switch (facing) {
			case NORTH:
				// 朝北 - 默认方向，不需要旋转
				break;
			case SOUTH:
				// 朝南 - 旋转180度
				poseStack.mulPose(new Quaternion(new Vector3f(0, 1, 0), 180, true));
				break;
			case WEST:
				// 朝西 - 旋转90度
				poseStack.mulPose(new Quaternion(new Vector3f(0, 1, 0), 90, true));
				break;
			case EAST:
				// 朝东 - 旋转-90度（或270度）
				poseStack.mulPose(new Quaternion(new Vector3f(0, 1, 0), -90, true));
				break;
			default:
				// 其他方向（上下）不做处理
				break;
		}
	}
	private void positionByFacing(PoseStack poseStack, Direction facing) {
		switch (facing) {
			case NORTH:
				poseStack.translate(0.5f, 0.45f, 0.56f); // 北面靠前一点
				break;
			case SOUTH:
				poseStack.translate(0.5f, 0.45f, 0.437f); // 南面靠后一点
				break;
			case WEST:
				poseStack.translate(0.56f, 0.45f, 0.5f); // 西面靠左一点
				break;
			case EAST:
				poseStack.translate(0.436f, 0.45f, 0.5f); // 东面靠右一点
				break;
			default:
				poseStack.translate(0.5f, 0.45f, 0.5f);
				break;
		}
	}

	@Override
	public RenderType getRenderType(FullbottlePurifierBlockEntity animatable, float partialTick, PoseStack poseStack,
									MultiBufferSource bufferSource, VertexConsumer buffer,
									int packedLight, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));  // 关键！
	}
}
