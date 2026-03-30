package cheng.build.datagen;

import cheng.build.init.InitBlock;
import com.machinezoo.noexception.throwing.ThrowingRunnable;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;

import java.util.Iterator;
import java.util.function.BiConsumer;

public class ModBlockLoot extends BlockLoot {

    @Override
    protected void addTables() {
        this.dropSelf(InitBlock.fullbottle_purifier.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return InitBlock.register.getEntries().stream()
                .map(RegistryObject::get)
                .toList();
    }
}