package cheng.build.init;

import cheng.build.Build;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

public class InitSound {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Build.MODID);

    public static final RegistryObject<SoundEvent> EMPTY = register("null");
    public static final RegistryObject<SoundEvent> build_driver_equie = register("build_driver_equie");
    public static final RegistryObject<SoundEvent> best_match = register("best_match");
    public static final RegistryObject<SoundEvent> rabbat = register("rabbat");
    public static final RegistryObject<SoundEvent> tank = register("tank");

    public static final Map<String, String> SOUNDS_FOR_DATAGEN = Map.of(
            "build_driver_equie", "build_driver_equie",
            "best_match", "best_match",
            "rabbat", "rabbat",
            "tank", "tank"
    );

    private static RegistryObject<SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(Build.MODID, name)));
    }

    // 获取 ResourceLocation 的方法
    public static ResourceLocation getSoundId(String soundName) {
        return new ResourceLocation(Build.MODID, soundName);
    }
}
