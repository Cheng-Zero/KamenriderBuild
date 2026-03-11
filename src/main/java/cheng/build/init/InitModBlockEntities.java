package cheng.build.init;

import cheng.build.Build;
import cheng.build.block.FullbottlePurifierBlockEntity;
import cheng.build.block.FullbottlePurifierBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.function.Supplier;

public class InitModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Build.MODID);
	public static final RegistryObject<BlockEntityType<FullbottlePurifierBlockEntity>> fullbottle_purifler =
			REGISTRY.register("fullbottle_purifler", () -> BlockEntityType.Builder.of(FullbottlePurifierBlockEntity::new,
					InitBlock.fullbottle_purifier.get()).build(null));

	public static Map<Supplier<BlockEntityType>, BlockEntityRendererProvider> renderer_FOR_DATAGEN = Map.of(
            fullbottle_purifler::get, FullbottlePurifierBlockRenderer::new
	);

	private static RegistryObject<BlockEntityType<?>> register(String registryname, RegistryObject<Block> block, BlockEntityType.BlockEntitySupplier<?> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}
}
