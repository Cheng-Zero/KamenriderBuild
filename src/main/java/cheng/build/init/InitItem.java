package cheng.build.init;

import cheng.build.armor.*;
import cheng.build.block.FullbottlePurifierBlockItem;
import cheng.build.bottle.bottles.EmptyBottle;
import cheng.build.bottle.bottles.Rabbat;
import cheng.build.bottle.bottles.SmashBottle;
import cheng.build.bottle.bottles.Tank;
import cheng.build.render.BuildARMORRenderer;
import cheng.build.render.BuildDriverRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

import java.util.Map;
import java.util.function.Supplier;

import static cheng.build.Build.MODID;

public class InitItem {
    public static final DeferredRegister<Item> register = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<BuildDriver> buildDriver = registry("build_driver", BuildDriver::new);
    public static final RegistryObject<FullbottlePurifierBlockItem> fullbottle_purifier = registry("fullbottle_purifier", ()-> new FullbottlePurifierBlockItem(InitBlock.fullbottle_purifier.get(),new Item.Properties()));

    public static final RegistryObject<EmptyBottle>
            empty_bottle = bottle("empty",EmptyBottle::new);

    public static final RegistryObject<SmashBottle>
            smash_bottle = bottle("smash",SmashBottle::new);

    public static final RegistryObject<Rabbat>
            rabbat = bottle("rabbat", Rabbat::new);

    public static final RegistryObject<Tank>
            tank = bottle("tank", Tank::new);

    public static final RegistryObject<BuildArmor>
            buildBaseArmor = registry("base_armor", BuildBaseArmor::new),
            buildTankArmor = registry("tank_armor", BuildTankArmor::new),
            buildRabbatArmor = registry("rabbat_armor", BuildRabbatArmor::new);

    private static <T extends Item> RegistryObject<T> registry(String name,Supplier<T> supplier){
        try{ return register.register(name,supplier);}
        catch (Exception e) {throw new RuntimeException(e);}
    }

    public record Model(ResourceLocation geo,ResourceLocation anima,ResourceLocation texture) {}
    public static Model
            BuildDriver = new Model(GeoModel("build_driver"), GeoAnimation("build_driver"), GeoTexture("driver/build_driver")),
            BuildBase = new Model(GeoModel("base_armor"),GeoAnimation("base_armor"),GeoTexture("armor/base_armor")),
            BuildRabbatArmor = new Model(GeoModel("rabbat_armor"),GeoAnimation("rabbat_armor"),GeoTexture("armor/rabbat_armor")),
            BuildTankArmor = new Model(GeoModel("tank_armor"),GeoAnimation("tank_armor"),GeoTexture("armor/tank_armor"));

    public static Map<Class<? extends ARMOR>, Supplier<GeoArmorRenderer>> renderer_FOR_DATAGEN = Map.of(
            BuildDriver.class, ()->d(BuildDriver),
            BuildBaseArmor.class, ()->a(BuildBase),
            BuildRabbatArmor.class, ()->a(BuildRabbatArmor),
            BuildTankArmor.class, ()->a(BuildTankArmor)
    );

    private static BuildARMORRenderer a(Model model){
        return new BuildARMORRenderer(model.geo,model.texture,model.anima);
    }
    private static BuildDriverRenderer d(Model model){
        return new BuildDriverRenderer(model.geo,model.texture,model.anima);
    }
    private static ResourceLocation GeoModel(String path){
        return new ResourceLocation(MODID,"geo/"+path+".geo.json");
    }
    private static ResourceLocation GeoAnimation(String path){
        return new ResourceLocation(MODID,"animations/"+path+".animation.json");
    }
    private static ResourceLocation GeoTexture(String path){
        return new ResourceLocation(MODID,"textures/"+path+".png");
    }
    private static <T extends Item> RegistryObject<T> bottle(String name,Supplier<T> supplier){
        return registry(name+"_bottle",supplier);
    }
}
