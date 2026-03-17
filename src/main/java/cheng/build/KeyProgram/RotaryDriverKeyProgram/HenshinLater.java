package cheng.build.KeyProgram.RotaryDriverKeyProgram;

import cheng.build.api.IFullBottle;
import cheng.build.item.armor.BuildDriver;
import cheng.build.player_animation.PlayerAnimationUtil;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class HenshinLater extends RotaryDriver{
    private static List<IFullBottle> cachedBottles;
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

        ItemStack inorganic = loadItem(driverTag, BuildDriver.inorganicMatter_item_Name);
        ItemStack organic = loadItem(driverTag, BuildDriver.organicMatter_item_Name);

        driverTag.putBoolean("isUse", false);
    }
}
