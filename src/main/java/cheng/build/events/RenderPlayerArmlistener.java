package cheng.build.events;

import cheng.build.item.armor.base.BuildArmor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class RenderPlayerArmlistener {
    @SubscribeEvent
    public static void renderArmEvent(RenderArmEvent event) {
        AbstractClientPlayer player = event.getPlayer();
        if (player == null) return;

        ItemStack
                head = player.getItemBySlot(EquipmentSlot.HEAD),
                chest = player.getItemBySlot(EquipmentSlot.CHEST),
                feet = player.getItemBySlot(EquipmentSlot.FEET);

        // 只有穿上 BuildArmor 时才渲染自定义手臂
        if (!(head.getItem() instanceof BuildArmor)) {
            return;  // 不穿时，让原版正常渲染
        }
        if (!(chest.getItem() instanceof BuildArmor)) {
            return;  // 不穿时，让原版正常渲染
        }
        if (!(feet.getItem() instanceof BuildArmor)) {
            return;  // 不穿时，让原版正常渲染
        }

        if (hasMod())return;

        Minecraft mc = Minecraft.getInstance();
        EntityRenderDispatcher entityRenderDispatcher = mc.getEntityRenderDispatcher();
        // 获取盔甲渲染器
        PlayerRenderer playerRenderer = (PlayerRenderer) entityRenderDispatcher.getRenderer(player);

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource bufferSource = event.getMultiBufferSource();
        int packedLight = event.getPackedLight();

        poseStack.pushPose();
        poseStack.translate(0.0F, 1.5F, 0.0F);
        poseStack.scale(-1.0F, -1.0F, 1.0F);

        Render(event,player,head,EquipmentSlot.HEAD,playerRenderer,poseStack,bufferSource,packedLight);
        Render(event,player,chest,EquipmentSlot.CHEST,playerRenderer,poseStack,bufferSource,packedLight);
        Render(event,player,feet,EquipmentSlot.FEET,playerRenderer,poseStack,bufferSource,packedLight);

        poseStack.popPose();

        // 取消原版手臂渲染
        event.setCanceled(true);
    }
    private static boolean hasMod(){
        return ModList.get().isLoaded("hero_core_api") || ModList.get().isLoaded("rider_api");
    }

    private static void Render(RenderArmEvent event,Player player,ItemStack itemStack,EquipmentSlot equipmentSlot,PlayerRenderer playerRenderer,PoseStack poseStack,MultiBufferSource bufferSource,int packedLight){
        Model HeadarmorModel = ForgeHooksClient.getArmorModel(player, itemStack, equipmentSlot, playerRenderer.getModel());
        if (HeadarmorModel instanceof GeoArmorRenderer model) {
            GeoModel ChestgeoModel = model.getGeoModelProvider().getModel(
                    model.getGeoModelProvider().getModelLocation(itemStack.getItem())
            );

            // 隐藏不需要的骨骼
            ChestgeoModel.getBone(model.headBone).ifPresent(b -> b.setHidden(true));
            ChestgeoModel.getBone(model.bodyBone).ifPresent(b -> b.setHidden(true));
            ChestgeoModel.getBone(model.rightLegBone).ifPresent(b -> b.setHidden(true));
            ChestgeoModel.getBone(model.leftLegBone).ifPresent(b -> b.setHidden(true));

            // 只显示当前手臂
            boolean isRight = event.getArm() == HumanoidArm.RIGHT;
            ChestgeoModel.getBone(model.rightArmBone).ifPresent(b -> b.setHidden(!isRight));
            ChestgeoModel.getBone(model.leftArmBone).ifPresent(b -> b.setHidden(isRight));

            if (isRight) {
                ChestgeoModel.getBone(model.rightArmBone).ifPresent(b -> {
                    b.setRotationX(0); b.setRotationY(0); b.setRotationZ(0);
                });
            } else {
                ChestgeoModel.getBone(model.leftArmBone).ifPresent(b -> {
                    b.setRotationX(0); b.setRotationY(0); b.setRotationZ(0);
                });
            }
            Color renderColor = Color.WHITE;
            VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityTranslucent(model.getTextureLocation((BuildArmor) itemStack.getItem())));

            model.render(
                    ChestgeoModel, itemStack.getItem(), 0, RenderType.entityTranslucent(model.getTextureLocation((BuildArmor) itemStack.getItem())),
                    poseStack, bufferSource, buffer, packedLight, OverlayTexture.NO_OVERLAY,
                    renderColor.getRed() / 255F, renderColor.getGreen() / 255F,
                    renderColor.getBlue() / 255F, renderColor.getAlpha() / 255F
            );
        }
    }
}