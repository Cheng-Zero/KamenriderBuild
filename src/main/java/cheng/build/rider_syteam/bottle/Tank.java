package cheng.build.rider_syteam.bottle;

import cheng.build.api.IInorganicMatterBottle;
import cheng.build.init.InitItem;
import cheng.build.init.InitMobEffect;
import cheng.build.init.InitSound;
import cheng.build.item.armor.base.BuildArmor;
import cheng.build.item.bottle.bottle.FullBottle;
import cheng.build.item.bottle.bottle_effect.BottleMobEffect;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class Tank implements IInorganicMatterBottle {

    @Override
    public String getName() {
        return "Tank";
    }
    @Override
    public FullBottle getFullBottle() {
        return InitItem.tank.get();
    }

    @Override
    public BuildArmor getBuildArmor() {
        return InitItem.buildTankArmor.get();
    }

    @Override
    public BottleMobEffect BottleMobEffect() {
        return InitMobEffect.tank_effect.get();
    }

    @Override
    public SoundEvent sound() {
        return InitSound.tank.get();
    }
}
