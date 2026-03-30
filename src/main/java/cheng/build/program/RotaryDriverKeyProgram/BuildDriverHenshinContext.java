package cheng.build.program.RotaryDriverKeyProgram;

import cheng.build.Build;
import cheng.build.DelayedTask;
import cheng.build.data.DataManager;
import cheng.build.data.PlayerBuildData;
import cheng.build.entity.BuildUpEffectEntity;
import cheng.build.entity.EffectEntity;
import cheng.build.init.InitItem;
import cheng.build.item.armor.BuildDriver;
import cheng.build.player_animation.PlayerAnimationUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class BuildDriverHenshinContext extends RotaryDriver implements IHenshin {
    protected static final String
            Animation_start = "start",
            Animation_standby_start = "standby_start",
            Animation_standbying = "standbying",
            Animation_build_up = "build_up",
            Animation_build_up_close = "build_up_close",
            Animation_build_up_over = "build_up_over";

    /**
     * 变身前摇动
     */
    public void startHenshin() {
        data = DataManager.get(player);
        CompoundTag driverTag = player.getItemBySlot(EquipmentSlot.LEGS).getTag();
        if (driverTag == null) {
            driverTag = player.getItemBySlot(EquipmentSlot.LEGS).getOrCreateTag();
        }

        boolean empty =
                driverTag.getCompound(BuildDriver.organicMatter_item_Name).isEmpty() ||
                        driverTag.getCompound(BuildDriver.inorganicMatter_item_Name).isEmpty(),
                empty_Bottle =
                        loadItem(driverTag, BuildDriver.organicMatter_item_Name).getItem() == InitItem.empty_bottle.get() ||
                                loadItem(driverTag, BuildDriver.inorganicMatter_item_Name).getItem() == InitItem.empty_bottle.get();

        if (hasBuildUpEntity()) return;
        data.setCurrentMode(PlayerBuildData.TransformMode.HENSHIN_COMPLETE);

        if (player instanceof ServerPlayer serverPlayer)
            PlayerAnimationUtil.playanimation(serverPlayer, "animation.round", true);

        data.setDriverInUse(true);

        if (empty) return;
        if (empty_Bottle) return;

        BuildUpEffectEntity.addEntity(effect(), player);

        effect().setAnimation(Animation_start);

        DelayedTask.chain(player.level)
                .then(40, () -> effect().setAnimation(Animation_standby_start))
                .then(1, () -> effect().setAnimation(Animation_standbying))
                .start();
    }

    /**
     * 变身前摇动完成
     */
    public void stopHenshin() {
        data = DataManager.get(player);
        CompoundTag driverTag = data.getDriverTag();

        ItemStack
                inorganic = loadItem(driverTag, BuildDriver.inorganicMatter_item_Name),
                organic = loadItem(driverTag, BuildDriver.organicMatter_item_Name);

        boolean onFullBottle = organic.isEmpty() && inorganic.isEmpty();

        if (!onFullBottle && driverTag.getBoolean("isUse")) {
            summonEffectEntity();
            Summon(Animation_build_up, Animation_build_up_close, Animation_build_up_over);
            data.setCurrentMode(PlayerBuildData.TransformMode.HENSHIN_IDLE);
            data.setDriverInUse(false);
            PlayerAnimationUtil.playanimation(player, "animation.round", false);
            return;
        }
        data.setCurrentMode(PlayerBuildData.TransformMode.IDLE);
        data.setDriverInUse(false);
        PlayerAnimationUtil.playanimation(player, "animation.round", false);
    }

    /**
     * 变身后摇动
     */
    public void startRound() {
        data = DataManager.get(player);
        CompoundTag driverTag = data.getDriverTag();
        if (driverTag.getBoolean("isUse")) return;
        data = DataManager.get(player);
        CurrunStartTime = (int) player.level.getGameTime();
        data.setDriverInUse(true);
        PlayerAnimationUtil.playanimation(player, "animation.round", true);
        data.setCurrentMode(PlayerBuildData.TransformMode.SKILL_COMPLETE);
    }

    /**
     * 变身后摇动完成
     */
    public void stopRound() {
        data = DataManager.get(player);
        CompoundTag driverTag = player.getItemBySlot(EquipmentSlot.LEGS).getTag();
        if (driverTag == null) {
            driverTag = player.getItemBySlot(EquipmentSlot.LEGS).getOrCreateTag();
        }

        CurrunEndTime = (int) player.level.getGameTime();
        // 摇动时间
        data = DataManager.get(player);
        var helmetStack = player.getItemBySlot(EquipmentSlot.HEAD);
        var chestplateStack = player.getItemBySlot(EquipmentSlot.CHEST);

        int UseTime = CurrunEndTime - CurrunStartTime;

        ItemStack
                inorganic = loadItem(driverTag, BuildDriver.inorganicMatter_item_Name),
                organic = loadItem(driverTag, BuildDriver.organicMatter_item_Name);

        boolean empty =
                driverTag.getCompound(BuildDriver.organicMatter_item_Name).isEmpty() ||
                        driverTag.getCompound(BuildDriver.inorganicMatter_item_Name).isEmpty(),
                empty_Bottle =
                        loadItem(driverTag, BuildDriver.organicMatter_item_Name).getItem() == InitItem.empty_bottle.get() ||
                                loadItem(driverTag, BuildDriver.inorganicMatter_item_Name).getItem() == InitItem.empty_bottle.get();

        PlayerAnimationUtil.playanimation(player, "animation.round", false);
        data.setCurrentMode(PlayerBuildData.TransformMode.HENSHIN_IDLE);
        data.setDriverInUse(false);

        if (empty_Bottle || empty) return;

        if (!(CurrentFormNotBottle(inorganic, chestplateStack) && CurrentFormNotBottle(organic, helmetStack))){
            return;
        }else if (CurrentFormNotBottle(inorganic, chestplateStack) || CurrentFormNotBottle(organic, helmetStack)) {
            HenshinStop();
            return;
        }
    }

    private void HenshinStart() {
    }

    private void HenshinStop() {
        summonEffectEntity();
        DelayedTask.run(player.level, 60, () -> HenshinBeforeEquieBottleArmor(false));
        PlayerAnimationUtil.playanimation(player, "animation.round", false);
        data.setCurrentMode(PlayerBuildData.TransformMode.HENSHIN_IDLE);
        data.setDriverInUse(false);
        Build.LOGGER.debug("数据：{}", data.getCurrentMode());
    }

    private void FinshiSkillStart() {

    }

    private void FinshiSkillStop() {

    }

    /// 有BuildUp实体就返回，不进行操作
    private boolean hasBuildUpEntity() {
        for (EffectEntity en : effect().getEntity(player))
            if (en.getOwner() == player)
                return true;
        return false;
    }
}
