package cheng.build.init;

import cheng.build.Build;
import cheng.build.armor.*;
import cheng.build.entity.BuildUpEffectEntity;
import cheng.build.entity.BuildUpEffectRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class InitEntity {
    public static final DeferredRegister<EntityType<?>> registry = DeferredRegister.create(ForgeRegistries.ENTITIES, Build.MODID);
    public static final RegistryObject<EntityType<BuildUpEffectEntity>> build_up = buildEntity("build_up_effect_entity",BuildUpEffectEntity::new,5f,3f);

    public static Map<Supplier<EntityType<? extends LivingEntity>>, EntityRendererProvider> renderer_FOR_DATAGEN = Map.of(
            build_up::get, BuildUpEffectRenderer::new
    );
    public static Map<Supplier<EntityType<? extends LivingEntity>>, Supplier<AttributeSupplier>> Attributes_FOR_DATAGEN = Map.of(
            build_up::get, ()->EntityAttributes.build_up
    );
    public static <T extends Entity> RegistryObject<EntityType<T>> buildEntity(String name,EntityType.EntityFactory<T> entity,
                                                                               float width, float height) {
        return registry.register(name,
                () -> EntityType.Builder.of(entity, MobCategory.CREATURE).sized(width, height).build(name));
    }
}
