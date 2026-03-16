package cheng.build;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

public class ForgeRegistriesHelper {
    public static Item Item(String modId, String itemId) {
        return Item(modId+":"+itemId);
    }
    public static Item Item(String itemId) {
        return Item(new ResourceLocation(itemId));
    }
    public static Item Item(ResourceLocation itemId) {
        return ForgeRegistries.ITEMS.getValue(itemId);
    }
    public static RecipeSerializer<?> RecipeSerializer(String itemId) {
        return ForgeRegistries.RECIPE_SERIALIZERS.getValue(new ResourceLocation(itemId));
    }
    public static SoundEvent SoundEvent(String itemId) {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(itemId));
    }
}
