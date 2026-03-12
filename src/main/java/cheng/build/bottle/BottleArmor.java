package cheng.build.bottle;

import cheng.build.Build;
import cheng.build.GeoModelPath;
import cheng.build.armor.BuildArmor;
import cheng.build.bottle.bottles.Bottle;
import cheng.build.init.InitItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BottleArmor {
    private static final Map<Bottle,BottleArmor> BottleBottleArmor = new HashMap<>();
    private static final Map<BuildArmor,BottleArmor> BuildArmorBottleArmor = new HashMap<>();
    private static final List<BottleArmor> armorSet = new ArrayList<>();
    static {
        addBottleArmor(InitItem.rabbat.get(), InitItem.buildRabbatArmor.get(), GeoModelPath.BuildRabbatArmor.texture());
        addBottleArmor(InitItem.tank.get(), InitItem.buildTankArmor.get(), GeoModelPath.BuildTankArmor.texture());
    }
    protected final Bottle bottle;
    protected final BuildArmor armorItem;
    protected final ResourceLocation texture;

    private BottleArmor(Bottle bottle, BuildArmor armorItem, ResourceLocation texture) {
        this.bottle = bottle;
        this.armorItem = armorItem;
        this.texture = texture;
    }

    public static void addBottleArmor(Bottle bottle,BuildArmor armorItem,ResourceLocation texture){
        if (BottleBottleArmor.containsKey(bottle) || BuildArmorBottleArmor.containsKey(armorItem)) {
            // 可以添加日志警告
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
