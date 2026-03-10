package cheng.build.init;

import cheng.build.Build;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.Set;

public class InitSound {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Build.MODID);

    public static final RegistryObject<SoundEvent>
            EMPTY = register("null"),
            build_driver_equie = register("build_driver_equie"),
            best_match = register("best_match"),
            rabbat = register("rabbat"),
            tank = register("tank");

    public static final Set<String> Sound_For_Datagen = Set.of(
            "build_driver_equie",
            "best_match",
            "rabbat",
            "tank"
    );

//    public static final Map<String, String> SOUNDS_FOR_DATAGEN = Map.of(
//            Sound.forEach((d)->),
//            "build_driver_equie", "build_driver_equie",
//            "best_match", "best_match",
//            "rabbat", "rabbat",
//            "tank", "tank"
//    );


    private static RegistryObject<SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(Build.MODID, name)));
    }

    // 获取 ResourceLocation 的方法
    public static ResourceLocation getSoundId(String soundName) {
        return new ResourceLocation(Build.MODID, soundName);
    }
}
