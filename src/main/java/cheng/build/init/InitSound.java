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
            experimental_form = register("experimental_form"),
            best_match = register("best_match"),
            super_best_match = register("super_best_match"),
            rabbat = register("rabbat"),
            tank = register("tank"),
            gorilla = register("gorilla"),
            diamond = register("diamond"),
            shake_bottle = register("shake_bottle"),
            don_ten_kan = register("don_ten_kan"),
            best_match_rabbat_tank = register("best_match_rabbat_tank"),
            best_match_gorilla_diamond = register("best_match_gorilla_diamond")
            ;

    public static final Set<String> Sound_For_Datagen = Set.of(
            "build_driver_equie",
            "experimental_form",
            "best_match",
            "super_best_match",
            "rabbat",
            "tank",
            "gorilla",
            "diamond",
            "shake_bottle",
            "don_ten_kan",
            "best_match_rabbat_tank",
            "best_match_gorilla_diamond"
    );

    private static RegistryObject<SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(Build.MODID, name)));
    }

    // 获取 ResourceLocation 的方法
    public static ResourceLocation getSoundId(String soundName) {
        return new ResourceLocation(Build.MODID, soundName);
    }
}
