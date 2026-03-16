package cheng.build;

import net.minecraft.resources.ResourceLocation;

import static cheng.build.Build.MODID;

public class GeoModelPath {
    public record model(ResourceLocation model, ResourceLocation animation, ResourceLocation texture) {}

    public static final model
            // 物品
            empty_bottle = bottle("empty_bottle"),
            smash_bottle = bottle("smash_bottle","smash_bottle","smash_bottle"),
            RabbatBottle = bottle("rabbat_bottle"),
            TankBottle = bottle("tank_bottle"),
            GorillaBottle = bottle("gorilla_bottle"),
            DiamondBottle = bottle("diamond_bottle"),
            build_up = render("build_up_effect_entity", "build_up_effect_entity", "entity/build_up_effect_entity"),
            fullbottle_purifier = render("fullbottle_purifier", "fullbottle_purifier", "blocks/fullbottle_purifier"),

            BuildDriver = render("build_driver","build_driver","driver/build_driver"),
            BuildBase = render("base_armor","base_armor","armor/base_armor"),
            BuildRabbatArmor = render("rabbat_armor","rabbat_armor","armor/rabbat_armor"),
            BuildTankArmor = render("tank_armor","tank_armor","armor/tank_armor"),
            BuildGorillaArmor = render("gorilla_armor","gorilla_armor","armor/gorilla_armor"),
            BuildDiamondArmor = render("diamond_armor","diamond_armor","armor/diamond_armor");

    public static model bottle(String texture) {
        return bottle("bottle", "bottle", texture);
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
    private static ResourceLocation GeoModel(String path) {return new ResourceLocation(MODID, "geo/" + path + ".geo.json");}
    private static ResourceLocation GeoAnimation(String path) {return new ResourceLocation(MODID, "animations/" + path + ".animation.json");}
    private static ResourceLocation GeoTexture(String path) {return new ResourceLocation(MODID, "textures/" + path + ".png");}
}