package cheng.build.init;

import cheng.build.GeoModelPath;
import cheng.build.item.armor.*;
import cheng.build.item.armor.base.ARMOR;
import cheng.build.item.armor.base.BuildArmor;
import cheng.build.block.FullbottlePurifierBlockItem;
import cheng.build.item.bottle.bottle.InorganicMatterBottleItem;
import cheng.build.item.bottle.bottle.OrganicMatterBottleItem;
import cheng.build.client.renderer.BuildARMORRenderer;
import cheng.build.client.renderer.BuildDriverRenderer;
import cheng.build.item.bottle.bottles.*;
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
    public static final RegistryObject<FullbottlePurifierBlockItem> fullbottle_purifier = registry("fullbottle_purifier", FullbottlePurifierBlockItem::new);

    public static final RegistryObject<EmptyBottleItem>
            empty_bottle = bottle("empty", EmptyBottleItem::new);

    public static final RegistryObject<SmashBottleItem>
            smash_bottle = bottle("smash", SmashBottleItem::new);

    public static final RegistryObject<OrganicMatterBottleItem>
            rabbat = bottle("rabbat", RabbatItem::new),
            gorilla = bottle("gorilla", GorillaItem::new)
    ;

    public static final RegistryObject<InorganicMatterBottleItem>
            tank = bottle("tank", TankItem::new),
            diamond = bottle("diamond", DiamondItem::new)
    ;

    public static final RegistryObject<BuildArmor>
            buildBaseArmor = registry("base_armor", BuildBaseArmor::new),
            buildTankArmor = registry("tank_armor", BuildTankArmor::new),
            buildRabbatArmor = registry("rabbat_armor", BuildRabbatArmor::new),
            buildGorillaArmor = registry("gorilla_armor", BuildGorillaArmor::new),
            buildDiamondArmor = registry("diamond_armor", BuildDiamondArmor::new)
                    ;

    private static <T extends Item> RegistryObject<T> registry(String name,Supplier<T> supplier){
        try{ return register.register(name,supplier);}
        catch (Exception e) {throw new RuntimeException(e);}
    }

    public static Map<Class<? extends ARMOR>, Supplier<GeoArmorRenderer>> renderer_FOR_DATAGEN = Map.of(
            BuildDriver.class, ()->driver(GeoModelPath.BuildDriver),
            BuildBaseArmor.class, ()->armor(GeoModelPath.BuildBase),
            BuildRabbatArmor.class, ()->armor(GeoModelPath.BuildRabbatArmor),
            BuildTankArmor.class, ()->armor(GeoModelPath.BuildTankArmor),
            BuildGorillaArmor.class, ()->armor(GeoModelPath.BuildGorillaArmor),
            BuildDiamondArmor.class, ()->armor(GeoModelPath.BuildDiamondArmor)
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
