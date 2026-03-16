package cheng.build.datagen;

import cheng.build.Build;
import cheng.build.init.InitSound;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class SoundProvider extends SoundDefinitionsProvider {
    /**
     * Creates a new instance of this data provider.
     *
     * @param generator The data generator instance provided by the event you are initializing this provider in.
     * @param modId     The mod ID of the current mod.
     * @param helper    The existing file helper provided by the event you are initializing this provider in.
     */
    public SoundProvider(DataGenerator generator, String modId, ExistingFileHelper helper) {
        super(generator, modId, helper);
    }
    @Override
    public void registerSounds() {
        // 直接遍历声音名称，不使用 RegistryObject
        InitSound.Sound_For_Datagen.forEach((registryName) -> {
            add(registryName, createSoundDefinition(registryName));
        });
    }

    private SoundDefinition createSoundDefinition(String soundFileName) {
        // 创建声音定义
        ResourceLocation soundLocation = new ResourceLocation(Build.MODID, soundFileName);

        return SoundDefinition.definition()
                .with(SoundDefinition.Sound.sound(
                        soundLocation,
                        SoundDefinition.SoundType.SOUND
                ).stream());
    }
}