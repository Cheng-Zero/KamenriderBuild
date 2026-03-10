package cheng.build;

import net.minecraft.resources.ResourceLocation;

import static cheng.build.Build.MODID;

public class GeoModelPath {
    public record model(ResourceLocation model, ResourceLocation animation, ResourceLocation texture) {}

    public static final ResourceLocation RabbatBottleTexture = BottleGeoTexture("rabbat_bottle");
    public static final ResourceLocation TankBottleTexture = BottleGeoTexture("tank_bottle");
    public static final model empty_bottle = bottle("empty_bottle", "empty_bottle", "empty_bottle");
    public static final model build_up = render("build_up_effect_entity", "build_up_effect_entity", "entity/build_up_effect_entity");
    public static final model fullbottle_purifier = render("fullbottle_purifier", "fullbottle_purifier", "blocks/fullbottle_purifier");

    public static model OrganicMatter(ResourceLocation texture) {
        return new model(BottleGeoModel("organic_matter_bottle"), BottleGeoAnimation("organic_matter_bottle"), texture);
    }

    public static model InorganicMatter(ResourceLocation texture) {
        return new model(BottleGeoModel("inorganic_matter_bottle"), BottleGeoAnimation("inorganic_matter_bottle"), texture);
    }

    private static model bottle(String model, String animation, String texture) {
        return new model(BottleGeoModel(model), BottleGeoAnimation(animation), BottleGeoTexture(texture));
    }

    private static model render(String model, String animation, String texture) {
        return new model(GeoModel(model), GeoAnimation(animation), GeoTexture(texture));
    }

    private static ResourceLocation BottleGeoModel(String path) {
        return GeoModel("bottles/" + path);
    }

    private static ResourceLocation BottleGeoAnimation(String path) {
        return GeoAnimation("bottles/" + path);
    }

    private static ResourceLocation BottleGeoTexture(String path) {
        return GeoTexture("bottles/" + path);
    }

    private static ResourceLocation GeoModel(String path) {
        return new ResourceLocation(MODID, "geo/" + path + ".geo.json");
    }

    private static ResourceLocation GeoAnimation(String path) {
        return new ResourceLocation(MODID, "animations/" + path + ".animation.json");
    }

    private static ResourceLocation GeoTexture(String path) {
        return new ResourceLocation(MODID, "textures/" + path + ".png");
    }
}