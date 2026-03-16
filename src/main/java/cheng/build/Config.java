package cheng.build;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Build.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final List<? extends String> list = List.of(
            "kamenrider_build:rabbat_bottle",
            "kamenrider_build:tank_bottle",
            "kamenrider_build:gorilla_bottle",
            "kamenrider_build:diamond_bottle"
    );

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    static final ForgeConfigSpec SPEC;

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS;

    private static final ForgeConfigSpec.DoubleValue FullBottlePurifierProgress;

    public static Set<Item> items;
    public static double fullbottlepurifierprogress;
    private static boolean validateItemName(final Object obj) {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        fullbottlepurifierprogress = FullBottlePurifierProgress.get();
        // convert the list of strings into a set of items
        items = ITEM_STRINGS.get()
                .stream()
                .map(ForgeRegistriesHelper::Item)
                .collect(Collectors.toSet());
    }
    static {
        BUILDER.push("满瓶净化机");
        ITEM_STRINGS = BUILDER.comment("满瓶净化机的净化表")
                .defineListAllowEmpty(List.of("items"), () -> list, Config::validateItemName);
        FullBottlePurifierProgress = BUILDER
                .comment("满瓶净化机需要的净化时间")
                .defineInRange("Tick",500,5.0,Double.MAX_VALUE);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}