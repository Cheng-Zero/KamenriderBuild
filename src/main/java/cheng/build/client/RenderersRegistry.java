package cheng.build.client;

import cheng.build.Build;
import cheng.build.GeoModelPath;
import cheng.build.client.renderer.BuildARMORRenderer;
import cheng.build.client.renderer.BuildDriverRenderer;
import cheng.build.init.InitEntity;
import cheng.build.init.InitModBlockEntities;
import cheng.build.item.armor.*;
import cheng.build.item.armor.base.ARMOR;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

import java.util.Map;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Build.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class RenderersRegistry {

    public static Map<Class<? extends ARMOR>, Supplier<GeoArmorRenderer>> renderer_FOR_DATAGEN = Map.of(
            BuildDriver.class, ()->driver(GeoModelPath.BuildDriver),
            BuildBaseArmor.class, ()->armor(GeoModelPath.BuildBase),
            BuildRabbatArmor.class, ()->armor(GeoModelPath.BuildRabbatArmor),
            BuildTankArmor.class, ()->armor(GeoModelPath.BuildTankArmor),
            BuildGorillaArmor.class, ()->armor(GeoModelPath.BuildGorillaArmor),
            BuildDiamondArmor.class, ()->armor(GeoModelPath.BuildDiamondArmor)
    );

    private static BuildARMORRenderer armor(GeoModelPath.model model){
        return new BuildARMORRenderer(model.model(),model.texture(),model.animation());
    }
    private static BuildDriverRenderer driver(GeoModelPath.model model){
        return new BuildDriverRenderer(model.model(),model.texture(),model.animation());
    }

    /// 盔甲绑定渲染
    @SubscribeEvent
    public static void registerAddLayerRenderers(final EntityRenderersEvent.AddLayers event) {
//        ServiceLoader<IModel> iModels = ServiceLoader.load(IModel.class);
//        iModels.stream().forEach((d)-> {
//            armor(d.get().getAll());
//        });
        renderer_FOR_DATAGEN.forEach(GeoArmorRenderer::registerArmorRenderer);
    }
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        InitEntity.renderer_FOR_DATAGEN.forEach((e, r)->
                event.registerEntityRenderer(e.get(),r));
        InitModBlockEntities.renderer_FOR_DATAGEN.forEach((blockentity, render)->
                event.registerBlockEntityRenderer(blockentity.get(), render));
    }
}
