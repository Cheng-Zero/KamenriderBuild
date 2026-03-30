package cheng.build.program;

import cheng.build.Build;
import cheng.build.api.IFullBottle;
import cheng.build.data.ABaseData;
import cheng.build.item.bottle.bottle.FullBottle;
import cheng.build.item.bottle.bottle.Bottle;
import cheng.build.player_animation.PlayerAnimationUtil;
import cheng.build.rider_syteam.BuildRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;

public class ShakeBottle extends ABaseData {
    public static int CurrunStartTime = 0;
    public static int CurrunEndTime = 0;
    public void on() {
        Item mainHand = player.getItemBySlot(EquipmentSlot.MAINHAND).getItem();
        Item offHand = player.getItemBySlot(EquipmentSlot.OFFHAND).getItem();
        boolean onMain = mainHand instanceof Bottle;
        boolean onOff = offHand instanceof Bottle;
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
        Item mainHand = player.getItemBySlot(EquipmentSlot.MAINHAND).getItem();
        Item offHand = player.getItemBySlot(EquipmentSlot.OFFHAND).getItem();
        playAni("ddd", false);
        Build.LOGGER.info("玩家{}已停止摇晃 满装瓶",player.getDisplayName().getString());
        boolean onMain = mainHand instanceof FullBottle;
        boolean onOff = offHand instanceof FullBottle;
        CurrunEndTime = (int) player.level.getGameTime();
        int time =CurrunEndTime-CurrunStartTime;
        if (onMain && onOff && mainHand instanceof FullBottle mainBottle && offHand instanceof FullBottle offBottle) {
            fullapply(mainBottle,time);
            fullapply(offBottle,time);
        } else if (onMain && mainHand instanceof FullBottle fullBottle) {
            fullapply(fullBottle,time);
        } else if (onOff && offHand instanceof FullBottle fullBottle) {
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
        IFullBottle byItem = BuildRegistry.FullBottlefindByItem(fullBottle);
        fullBottle.apply(byItem, player, time);
        Build.LOGGER.info("玩家{}获取到类型{}的满装瓶Buff {}秒", player.getDisplayName().getString(), byItem.getName(), time / 20);
    }
}
