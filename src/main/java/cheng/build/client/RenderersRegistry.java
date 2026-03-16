package cheng.build.client;

import cheng.build.Build;
import cheng.build.init.InitEntity;
import cheng.build.init.InitItem;
import cheng.build.init.InitModBlockEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Build.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class RenderersRegistry {
    /// 盔甲绑定渲染
    @SubscribeEvent
    public static void registerAddLayerRenderers(final EntityRenderersEvent.AddLayers event) {
        InitItem.renderer_FOR_DATAGEN.forEach(GeoArmorRenderer::registerArmorRenderer);
    }
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        InitEntity.renderer_FOR_DATAGEN.forEach((e, r)->
                event.registerEntityRenderer(e.get(),r));
        InitModBlockEntities.renderer_FOR_DATAGEN.forEach((blockentity, render)->
                event.registerBlockEntityRenderer(blockentity.get(), render));
    }
}
