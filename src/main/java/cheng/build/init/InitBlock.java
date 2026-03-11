package cheng.build.init;

import cheng.build.Build;
import cheng.build.block.FullbottlePurifierBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class InitBlock {
    public static final DeferredRegister<Block> register = DeferredRegister.create(ForgeRegistries.BLOCKS, Build.MODID);
    public static final RegistryObject<Block> fullbottle_purifier =
            register.register("fullbottle_purifier", FullbottlePurifierBlock::new);

    private static <T extends Block> void registerBlockItems(String name,RegistryObject<T> block) {
        InitItem.register.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<T> Registry(String name, Supplier<T> block){
        RegistryObject<T> blocks = register.register(name,block);
        registerBlockItems(name, blocks);
        return blocks;
    }
}
