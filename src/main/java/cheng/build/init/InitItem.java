package cheng.build.init;

import cheng.build.item.armor.*;
import cheng.build.item.armor.base.BuildArmor;
import cheng.build.block.FullbottlePurifierBlockItem;
import cheng.build.item.bottle.bottle.InorganicMatterBottleItem;
import cheng.build.item.bottle.bottle.OrganicMatterBottleItem;
import cheng.build.item.bottle.bottles.*;
import cheng.cheng_util.ChengRegistriesUtil;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static cheng.build.Build.MODID;

public class InitItem {
    public static final DeferredRegister<Item> register = ChengRegistriesUtil.deferredItemRegister(MODID);
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
        return ChengRegistriesUtil.registerItem(register,name,supplier);
    }

    private static <T extends Item> RegistryObject<T> bottle(String name,Supplier<T> supplier){
        return registry(name+"_bottle",supplier);
    }

}
