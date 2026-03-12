package cheng.build.init;

import cheng.build.GeoModelPath;
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

    public static Map<Class<? extends ARMOR>, Supplier<GeoArmorRenderer>> renderer_FOR_DATAGEN = Map.of(
            BuildDriver.class, ()->driver(GeoModelPath.BuildDriver),
            BuildBaseArmor.class, ()->armor(GeoModelPath.BuildBase),
            BuildRabbatArmor.class, ()->armor(GeoModelPath.BuildRabbatArmor),
            BuildTankArmor.class, ()->armor(GeoModelPath.BuildTankArmor)
    );

    private static BuildARMORRenderer armor(GeoModelPath.model model){
        return new BuildARMORRenderer(model.model(),model.texture(),model.animation());
    }
    private static BuildDriverRenderer driver(GeoModelPath.model model){
        return new BuildDriverRenderer(model.model(),model.texture(),model.animation());
    }
    private static <T extends Item> RegistryObject<T> bottle(String name,Supplier<T> supplier){
        return registry(name+"_bottle",supplier);
    }
}
