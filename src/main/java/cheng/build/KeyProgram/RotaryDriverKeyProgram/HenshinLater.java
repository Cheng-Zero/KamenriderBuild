package cheng.build.KeyProgram.RotaryDriverKeyProgram;

import cheng.build.player_animation.PlayerAnimationUtil;

public class HenshinLater extends RotaryDriver{
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

        driverTag.putBoolean("isUse", false);
    }
}
