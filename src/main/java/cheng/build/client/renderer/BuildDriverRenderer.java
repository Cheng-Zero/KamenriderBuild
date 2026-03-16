package cheng.build.client.renderer;

import cheng.build.Build;
import cheng.build.item.armor.BuildDriver;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class BuildDriverRenderer extends DRIVERRenderer<BuildDriver> {
	public BuildDriverRenderer(ResourceLocation model, ResourceLocation texture, ResourceLocation animation) {
		super(model, texture, animation);
	}

	@Override
	public void render(float partialTick, PoseStack poseStack, VertexConsumer buffer, int packedLight) {
		super.render(partialTick, poseStack, buffer, packedLight);

		MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

		String
				organicKey = BuildDriver.organicMatter_item_Name,
				inorganicKey = BuildDriver.inorganicMatter_item_Name;


		CompoundTag
				tag = Objects.requireNonNull(itemStack.getTag()),
				organicTag = tag.getCompound(organicKey),
				inorganicTag = tag.getCompound(inorganicKey),
				driverTag = Objects.requireNonNull(entityLiving.getItemBySlot(EquipmentSlot.LEGS).getTag());

		ItemStack
				organicMatter_item = BuildDriver.loadItem(driverTag, "organicMatter_item"),
				inorganicMatter_item = BuildDriver.loadItem(driverTag, "inorganicMatter_item");

		float
				scaleX = 0.4f,
				scaleY = 0.4f,
				scaleZ = 0.4f;
		double
				OrganicKeytranslateX = 0.03, InorganicKeytranslateX =  0.32,
				translateY = 1.20,
				translateZ = -0.42;
		// 判断-是否拥有 有机物Tag
		if (tag.contains(organicKey)) {
			// 非空判定
			if (!organicTag.isEmpty()) {
				// 保存渲染矩阵以正常渲染
				poseStack.pushPose();
				// 绑定跟随身体移动和旋转
				this.body.translateAndRotate(poseStack);

				poseStack.scale(scaleX, scaleY, scaleZ);
				poseStack.translate(OrganicKeytranslateX, translateY, translateZ);
				// 渲染有机物槽位满装瓶罐
				renderOrganicMatter(organicMatter_item,packedLight,poseStack,bufferSource);
				// 恢复渲染矩阵以融合渲染
				poseStack.popPose();
			}
		}
		// 判断-是否拥有 无机物Tag
		if (tag.contains(inorganicKey)){
			// 非空判定
			if (!inorganicTag.isEmpty()) {
				poseStack.pushPose();
				// 绑定跟随身体移动和旋转
				this.body.translateAndRotate(poseStack);

				poseStack.scale(scaleX, scaleY, scaleZ);
				poseStack.translate(InorganicKeytranslateX, translateY,translateZ);
				// 渲染无机物槽位满装瓶罐
				renderInorganicMatter(inorganicMatter_item,packedLight,poseStack,bufferSource);
				poseStack.popPose();
			}
		}
	}
	// 物品渲染-有机物槽位分支
	void renderOrganicMatter(ItemStack organicMatter, int packedLight,
							 PoseStack poseStack, MultiBufferSource bufferSource){
		try {
			this.renderBaseMatter(organicMatter, packedLight, poseStack, bufferSource);
		} catch (Exception e) {
			Build.LOGGER.debug("驱动器有机物槽位物品渲染失败");
		}
	}
	// 物品渲染-无机物槽位分支
	void renderInorganicMatter(ItemStack inorganicMatter, int packedLight,
							   PoseStack poseStack, MultiBufferSource bufferSource){
		try {
			this.renderBaseMatter(inorganicMatter, packedLight, poseStack, bufferSource);
		} catch (Exception e) {
			Build.LOGGER.debug("驱动器无机物槽位物品渲染失败");
		}
	}
	// 基础物品渲染-主渲染
	void renderBaseMatter(ItemStack FullBottle,int packedLight,PoseStack poseStack,MultiBufferSource bufferSource) {
		// 获取物品渲染
		try {
			ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

			// 5. 获取正确的光照（考虑环境光遮蔽）
			int overlay = LivingEntityRenderer.getOverlayCoords(entityLiving, 0.0F);

			itemRenderer.renderStatic(
					FullBottle,
					ItemTransforms.TransformType.FIXED,
					packedLight,
					overlay,
					poseStack,
					bufferSource,
					1);
		} catch (Exception e) {
			Build.LOGGER.debug("驱动器槽位物品渲染失败");
		}
	}
}