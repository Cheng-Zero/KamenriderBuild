package cheng.build.datagen;

import cheng.build.Build;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Build.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        String modid = Build.MODID;
        if (event.includeClient()) {
            generator.addProvider(new ModModelsProvider(generator, modid, existingFileHelper));

            generator.addProvider(new SoundProvider(generator, modid, existingFileHelper));

            generator.addProvider(new ModLangProvider.ModEnUsLangProvider(generator, modid));
            generator.addProvider(new ModLangProvider.ModZhCnLangProvider(generator, modid));
        }
        if (event.includeServer()) {
            // 注册战利品表生成器
            generator.addProvider(new ModLootTableProvider(generator));
        }
//        generator.addProvider(new BlockStates(generator,Build.MODID,existingFileHelper));
    }
}
