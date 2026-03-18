package cheng.build.KeyProgram.RotaryDriverKeyProgram;

import cheng.build.DelayedTask;
import cheng.build.api.IFullBottle;
import cheng.build.item.armor.BuildDriver;
import cheng.build.player_animation.PlayerAnimationUtil;
import cheng.build.rider_syteam.BottleRegistry;
import cheng.build.rider_syteam.HenshinUtil;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class HenshinLater extends RotaryDriver{
    ItemStack inorganic,organic;
    private static final List<IFullBottle> cachedBottles = BottleRegistry.getAllBottles();
    protected void round(){
        if (driverTag.getBoolean("isUse"))return;
        CurrunStartTime = (int) player.level.getGameTime();
        driverTag.putBoolean("isUse", true);
        PlayerAnimationUtil.playanimation(player,"a",true);
    }
    protected void stop(){
        CurrunEndTime = (int) player.level.getGameTime();
        // 摇动时间
        int UseTime = CurrunEndTime - CurrunStartTime;

        inorganic = loadItem(driverTag, BuildDriver.inorganicMatter_item_Name);
        organic = loadItem(driverTag, BuildDriver.organicMatter_item_Name);


        if (isNotCurrentForm())
            ifIshandleFormSwitch();

        driverTag.putBoolean("isUse", false);
    }

    void ifIshandleFormSwitch(){
        HenshinUtil util = new HenshinUtil();
        util.update(player);
        util.summonEffectEntity(false);
        DelayedTask.run(player.level,60,()-> util.HenshinBeforeEquieBottleArmor(false));
    }

    protected boolean isNotCurrentForm(){
        HenshinUtil util = new HenshinUtil();
        util.update(player);
        inorganic = loadItem(driverTag, BuildDriver.inorganicMatter_item_Name);
        organic = loadItem(driverTag, BuildDriver.organicMatter_item_Name);
        boolean in = false,or = false;
        for (IFullBottle cachedBottle : cachedBottles) {
            if (util.isHenshining(cachedBottle,inorganic)) {
                in = true;
                continue;
            }
        }
        for (IFullBottle cachedBottle : cachedBottles) {
            if (util.isHenshining(cachedBottle,organic)) {
                or = true;
                continue;
            }
        }
        return in && or;
    }
}
