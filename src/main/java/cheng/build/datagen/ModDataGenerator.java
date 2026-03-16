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
        generator.addProvider(new ModModelsProvider(generator, modid,existingFileHelper));

        generator.addProvider(new SoundProvider(generator, modid,existingFileHelper));

        generator.addProvider(new ModEnUsLangProvider(generator, modid,"en_us"));
        generator.addProvider(new ModZhCnLangProvider(generator, modid,"zh_cn"));
//        generator.addProvider(new BlockStates(generator,Build.MODID,existingFileHelper));
    }
}
