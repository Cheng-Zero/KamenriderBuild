package cheng.build.KeyProgram;

import cheng.build.Build;
import cheng.build.api.IFullBottle;
import cheng.build.bottle.BottleRegistry;
import cheng.build.data.ABaseData;
import cheng.build.item.bottle.bottle.FullBottle;
import cheng.build.item.bottle.bottle.Bottle;
import cheng.build.player_animation.PlayerAnimationUtil;
import net.minecraft.server.level.ServerPlayer;

import java.util.ServiceLoader;

public class ShakeBottle extends ABaseData {
    public static int CurrunStartTime = 0;
    public static int CurrunEndTime = 0;
    public void on() {
        boolean onMain = main instanceof Bottle;
        boolean onOff = off instanceof Bottle;
        Build.LOGGER.info("玩家{}摇晃满装瓶中",player.getDisplayName().getString());
        CurrunStartTime = (int) player.level.getGameTime();
        if (onMain && onOff)
            shakeMainOffHandBottle();
        else if (onMain)
            shakeMainHandBottle();
        else if (onOff)
            shakeOffHandBottle();
    }
    public void off() {
        playAni("ddd", false);
        Build.LOGGER.info("玩家{}已停止摇晃 满装瓶",player.getDisplayName().getString());
        boolean onMain = main instanceof FullBottle;
        boolean onOff = off instanceof FullBottle;
        CurrunEndTime = (int) player.level.getGameTime();
        int time =CurrunEndTime-CurrunStartTime;
        if (onMain && onOff && main instanceof FullBottle mainBottle && off instanceof FullBottle offBottle) {
            fullapply(mainBottle,time);
            fullapply(offBottle,time);
        } else if (onMain && main instanceof FullBottle fullBottle) {
            fullapply(fullBottle,time);
        } else if (onOff && off instanceof FullBottle fullBottle) {
            fullapply(fullBottle,time);
        }
    }

    private void shakeMainHandBottle(){
        playAni("ddd",true);
    }
    private void shakeOffHandBottle(){
        playAni("ddd",true);
    }
    private void shakeMainOffHandBottle(){
        playAni("ddd",true);
    }
    private void playAni(String a,boolean s){
        if (player instanceof ServerPlayer serverPlayer)
            PlayerAnimationUtil.playanimation(serverPlayer,a,s);
    }
    private void fullapply(FullBottle fullBottle, int time){
        IFullBottle byItem = BottleRegistry.findByItem(fullBottle);
        fullBottle.apply(byItem, player, time);
        Build.LOGGER.info("玩家{}获取到类型{}的满装瓶Buff {}秒", player.getDisplayName().getString(), byItem.getName(), time / 20);
    }
}
