package cheng.build.init;

import cheng.build.Build;
import cheng.build.block.entity.FullbottlePurifierBlockEntity;
import cheng.build.block.renderer.FullbottlePurifierBlockRenderer;
import cheng.cheng_util.ChengRegistriesUtil;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.entity.EntityType;
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
			ChengRegistriesUtil.registerBlockEntity(REGISTRY,"fullbottle_purifler",
					()->BlockEntityType.Builder.of(FullbottlePurifierBlockEntity::new,InitBlock.fullbottle_purifier.get()).build(null));


	public static Map<Supplier<BlockEntityType>, BlockEntityRendererProvider> renderer_FOR_DATAGEN = Map.of(
            fullbottle_purifler::get, FullbottlePurifierBlockRenderer::new
	);

	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String registryname, BlockEntityType.BlockEntitySupplier<T> blockEntity, Block block) {
		return ChengRegistriesUtil.registerBlockEntity(REGISTRY,registryname, blockEntity, block);
	}
}
