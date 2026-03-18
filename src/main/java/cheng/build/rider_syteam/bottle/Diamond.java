package cheng.build.rider_syteam.bottle;

import cheng.build.api.IFullBottle;
import cheng.build.api.IInorganicMatterBottle;
import cheng.build.init.InitItem;
import cheng.build.init.InitMobEffect;
import cheng.build.init.InitSound;
import cheng.build.item.armor.base.BuildArmor;
import cheng.build.item.bottle.bottle.FullBottle;
import cheng.build.item.bottle.bottle_effect.BottleMobEffect;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class Diamond implements IFullBottle {

    @Override
    public String getName() {
        return "Diamond";
    }

    @Override
    public FullBottle getFullBottle() {
        return InitItem.diamond.get();
    }

    @Override
    public BuildArmor getBuildArmor() {
        return InitItem.buildDiamondArmor.get();
    }

    @Override
    public BottleMobEffect BottleMobEffect() {
        return InitMobEffect.diamond_effect.get();
    }

    @Override
    public SoundEvent sound() {
        return InitSound.diamond.get();
    }
}
