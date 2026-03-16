package cheng.build.client.renderer;

import cheng.build.item.armor.base.ARMOR;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public abstract class DRIVERRenderer<T extends ARMOR> extends BASEARMORRenderer<T> {
    public DRIVERRenderer(ResourceLocation model, ResourceLocation texture, ResourceLocation animation) {
        super(model, texture, animation);
    }

    public GeoArmorRenderer<T> applySlot(EquipmentSlot slot) {
        super.applySlot(slot);
        this.setBoneVisibility(this.bodyBone, true);
        return this;
    }
}

//		CompoundTag inorganic = tag.getCompound(BuildDriver.inorganicMatter_item_Name);
//		CompoundTag organic = tag.getCompound(BuildDriver.organicMatter_item_Name);
//		ItemStack organicMatter_item = BuildDriver.loadItem(organic, "organicMatter_item");
//		if (tag.get(organicMatter_item_Name) != null && !tag.getCompound(organicMatter_item_Name).isEmpty()) {
//			itemRenderer.renderStatic(organicMatter_item,
//					ItemTransforms.TransformType.FIXED, packedLight, 1, poseStack,
//					Minecraft.getInstance().renderBuffers().bufferSource(), 1);
//		}


//			Color color = getRenderColor(currentArmorItem, partialTick, poseStack, null, buffer, packedLight);
//			RenderType type = getRenderType(currentArmorItem, partialTick, poseStack, null, buffer, packedLight, getTextureLocation(currentArmorItem));
//			renderer(model, currentArmorItem, partialTick, type, poseStack,
//					Minecraft.getInstance().renderBuffers().bufferSource(), buffer, packedLight,
//					OverlayTexture.NO_OVERLAY,
//					color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);

// 发光层
// 获取NBT数据
//		String organicMatter = "";
//		String inorganicMatter = "";
//
//		if (itemStack != null && itemStack.getTag() != null) {
//			organicMatter = itemStack.getTag().getString("organicMatter");
//			inorganicMatter = itemStack.getTag().getString("inorganicMatter");
//		}
//
//		if (organicMatter != null && !organicMatter.isEmpty()) {
//			renderBottleTexture(model, partialTick, poseStack, organicMatter);
//		}
//
//		// --- 第三遍渲染：无机物质瓶纹理（如果有）---
//		if (inorganicMatter != null && !inorganicMatter.isEmpty()) {
//			renderBottleTexture(model, partialTick, poseStack,  inorganicMatter);
//		}

//		poseStack.popPose();
