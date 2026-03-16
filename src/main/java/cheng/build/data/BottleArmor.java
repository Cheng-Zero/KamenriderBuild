package cheng.build.data;

import cheng.build.Build;
import cheng.build.GeoModelPath;
import cheng.build.item.armor.base.BuildArmor;
import cheng.build.item.bottle.bottle.Bottle;
import cheng.build.init.InitItem;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record BottleArmor(Bottle bottle,BuildArmor armorItem,ResourceLocation texture) {
    private static final Map<Bottle,BottleArmor> BottleBottleArmor = new HashMap<>();
    private static final Map<BuildArmor,BottleArmor> BuildArmorBottleArmor = new HashMap<>();
    private static final List<BottleArmor> armorSet = new ArrayList<>();

    static {
        addBottleArmor(InitItem.rabbat.get(), InitItem.buildRabbatArmor.get(), GeoModelPath.BuildRabbatArmor.texture());
        addBottleArmor(InitItem.tank.get(), InitItem.buildTankArmor.get(), GeoModelPath.BuildTankArmor.texture());
        addBottleArmor(InitItem.gorilla.get(), InitItem.buildGorillaArmor.get(), GeoModelPath.BuildGorillaArmor.texture());
        addBottleArmor(InitItem.diamond.get(), InitItem.buildDiamondArmor.get(), GeoModelPath.BuildDiamondArmor.texture());
    }

    public static void addBottleArmor(Bottle bottle,BuildArmor armorItem,ResourceLocation texture){
        if (BottleBottleArmor.containsKey(bottle) || BuildArmorBottleArmor.containsKey(armorItem)) {
            // 日志警告
            Build.LOGGER.debug("出现重复注册");
            return;
        }
        BottleArmor armor = new BottleArmor(bottle, armorItem, texture);
        BottleBottleArmor.put(bottle,armor);
        BuildArmorBottleArmor.put(armorItem,armor);
        armorSet.add(armor);
    }
    public static List<BottleArmor> bottleArmorList(){
        return armorSet;
    }
}
