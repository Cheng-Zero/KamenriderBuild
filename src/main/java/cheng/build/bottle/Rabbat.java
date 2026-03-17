package cheng.build.bottle;

import cheng.build.api.IOrganicMatterBottle;
import cheng.build.init.InitItem;
import cheng.build.init.InitMobEffect;
import cheng.build.init.InitSound;
import cheng.build.item.armor.base.BuildArmor;
import cheng.build.item.bottle.bottle.FullBottle;
import cheng.build.item.bottle.bottle_effect.BottleMobEffect;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class Rabbat implements IOrganicMatterBottle {
    @Override
    public String getName() {
        return "Rabbat";
    }
    @Override
    public FullBottle getFullBottle() {
        return InitItem.rabbat.get();
    }
    @Override
    public BuildArmor getBuildArmor() {
        return InitItem.buildRabbatArmor.get();
    }

    @Override
    public BottleMobEffect BottleMobEffect() {
        return InitMobEffect.rabbat_effect.get();
    }

    @Override
    public SoundEvent sound() {
        return InitSound.rabbat.get();
    }
}
