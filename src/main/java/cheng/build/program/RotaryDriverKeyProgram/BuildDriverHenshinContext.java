package cheng.build.program.RotaryDriverKeyProgram;

import cheng.build.Build;
import cheng.build.DelayedTask;
import cheng.build.entity.BuildUpEffectEntity;
import cheng.build.entity.EffectEntity;
import cheng.build.item.armor.BuildDriver;
import cheng.build.player_animation.PlayerAnimationUtil;
import cheng.build.rider_syteam.HenshinUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class BuildDriverHenshinContext extends RotaryDriver implements IHenshin{
    private static UUID HenshinStart;
    private static UUID HenshinStop;

    protected static final String
            Animation_start = "start",
            Animation_standby_start = "standby_start",
            Animation_standbying = "standbying",
            Animation_build_up = "build_up",
            Animation_build_up_close = "build_up_close",
            Animation_build_up_over = "build_up_over";

    @Override
    public void startHenshin() {
        boolean empty =
                driverTag.getCompound(BuildDriver.organicMatter_item_Name).isEmpty() ||
                        driverTag.getCompound(BuildDriver.inorganicMatter_item_Name).isEmpty();
        setCurrentMode(DriverMode.HENSHIN_START);
        if (hasBuildUpEntity()) return;

        if (player instanceof ServerPlayer serverPlayer)
            PlayerAnimationUtil.playanimation(serverPlayer, "animation.round", true);

        setDriverInUse(true);

        if (empty)return;
        BuildUpEffectEntity.addEntity(effect(), player);

        effect().setAnimation(Animation_start);

        HenshinStart = DelayedTask.chain(player.level)
                .then(40, () -> effect().setAnimation(Animation_standby_start))
                .then(1, () -> effect().setAnimation(Animation_standbying))
                .start();
    }

    @Override
    public void stopHenshin() {
        ItemStack
                inorganic = loadItem(driverTag, BuildDriver.inorganicMatter_item_Name),
                organic = loadItem(driverTag, BuildDriver.organicMatter_item_Name);
        HenshinUtil util = new HenshinUtil();
        util.update(player);

        boolean onFullBottle = organic.isEmpty() && inorganic.isEmpty();

        setCurrentMode(DriverMode.IDLE);

        if (!onFullBottle && driverTag.getBoolean("isUse")) {
            util.summonEffectEntity(true);
            util.Summon(Animation_build_up,Animation_build_up_close,Animation_build_up_over);
            RotaryDriver.setCurrentMode(DriverMode.HENSHIN_START);
        }
        setDriverInUse(false);
        if (player instanceof ServerPlayer serverPlayer)
            PlayerAnimationUtil.playanimation(serverPlayer, "animation.round", false);
    }

    @Override
    public void startRound() {
        if (driverTag.getBoolean("isUse"))return;
        CurrunStartTime = (int) player.level.getGameTime();
        setDriverInUse(true);
        PlayerAnimationUtil.playanimation(player,"animation.round",true);
        RotaryDriver.setCurrentMode(DriverMode.HENSHIN_LATER);
    }

    @Override
    public void stopRound() {
        CurrunEndTime = (int) player.level.getGameTime();
        // 摇动时间
        int UseTime = CurrunEndTime - CurrunStartTime;

        ItemStack
                inorganic = getInorganicBottle(),
                organic = getOrganicBottle();

        HenshinUtil util = new HenshinUtil();
        util.update(player);
        if (util.CurrentFormNotBottle(inorganic,chestplateStack) || util.CurrentFormNotBottle(organic,helmetStack))
            ifIshandleFormSwitch();

        PlayerAnimationUtil.playanimation(player,"animation.round",false);
        RotaryDriver.setCurrentMode(DriverMode.HENSHIN_START);
        setDriverInUse(false);
    }

    private void FinshiSkill(){

    }

    /// 有BuildUp实体就返回，不进行操作
    private boolean hasBuildUpEntity(){
        for (EffectEntity en : effect().getEntity(player))
            if (en.getOwner()==player)
                return true;
        return false;
    }

    private void ifIshandleFormSwitch(){
        HenshinUtil util = new HenshinUtil();
        util.update(player);
        util.summonEffectEntity(false);
        DelayedTask.run(player.level,60,()-> util.HenshinBeforeEquieBottleArmor(false));
    }
}
