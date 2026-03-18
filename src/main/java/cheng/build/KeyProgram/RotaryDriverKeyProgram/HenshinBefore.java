package cheng.build.KeyProgram.RotaryDriverKeyProgram;

import cheng.build.Build;
import cheng.build.DelayedTask;
import cheng.build.item.armor.BuildDriver;
import cheng.build.entity.BuildUpEffectEntity;
import cheng.build.entity.EffectEntity;
import cheng.build.player_animation.PlayerAnimationUtil;
import cheng.build.rider_syteam.HenshinUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class HenshinBefore extends RotaryDriver {
    protected void Henshin(){
        boolean empty =
                driverTag.getCompound(BuildDriver.organicMatter_item_Name).isEmpty() ||
                driverTag.getCompound(BuildDriver.inorganicMatter_item_Name).isEmpty();
        if (hasBuildUpEntity() || empty) return;

        if (player instanceof ServerPlayer serverPlayer)
            PlayerAnimationUtil.playanimation(serverPlayer, "animation.round", true);
        driverTag.putBoolean("isUse", true);

        BuildUpEffectEntity.addEntity(effect(), player);

        effect().setAnimation(Animation_start);

        DelayedTask.chain(player.level)
                .then(40, () -> effect().setAnimation(Animation_standby_start))
                .then(1, () -> effect().setAnimation(Animation_standbying))
                .start();
    }
    protected void stop(){
        ItemStack inorganic = loadItem(driverTag, BuildDriver.inorganicMatter_item_Name);
        ItemStack organic = loadItem(driverTag, BuildDriver.organicMatter_item_Name);
        HenshinUtil util = new HenshinUtil();
        util.update(player);

        boolean onFullBottle = organic.isEmpty() && inorganic.isEmpty();

        if (!onFullBottle && driverTag.getBoolean("isUse")) {
            Build.LOGGER.info("按键抬起成功");
            if (player instanceof ServerPlayer serverPlayer)
                PlayerAnimationUtil.playanimation(serverPlayer, "animation.round", false);

            util.summonEffectEntity(true);

            util.Summon(Animation_build_up,Animation_build_up_close,Animation_build_up_over);

            driverTag.putBoolean("isUse", false);
        }
    }

    /// 有BuildUp实体就返回，不进行操作
    private boolean hasBuildUpEntity(){
        for (EffectEntity en : effect().getEntity(player))
            if (en.getOwner()==player)
                return true;
        return false;
    }
}
