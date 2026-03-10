package cheng.build.init;

import cheng.build.Build;
import cheng.build.block.DwaTileRenderer;
import cheng.build.block.FullbottlePurifierEntity;
import cheng.build.entity.BuildUpEffectRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import java.util.Map;
import java.util.function.Supplier;

public class InitModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Build.MODID);
	public static final RegistryObject<BlockEntityType<?>> fullbottle_purifler = register("fullbottle_purifler",InitBlock.fullbottle_purifier,FullbottlePurifierEntity::new);

	public static Map<Supplier<BlockEntityType<? extends BlockEntity>>, BlockEntityRendererProvider> renderer_FOR_DATAGEN = Map.of(
            fullbottle_purifler, DwaTileRenderer::new
	);
	private static RegistryObject<BlockEntityType<?>> register(String registryname, RegistryObject<Block> block, BlockEntityType.BlockEntitySupplier<?> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}
}
