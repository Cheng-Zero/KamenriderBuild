package cheng.build.program.ClearKeyProgram;

import cheng.build.ArmorUseHandler;
import cheng.build.ItemHelper;
import cheng.build.data.DataManager;
import cheng.build.data.PlayerBuildData;
import cheng.build.item.armor.BuildDriver;
import cheng.build.data.ABaseData;
import net.minecraft.nbt.CompoundTag;

import java.util.Objects;

import static cheng.build.item.armor.BuildDriver.inorganicMatter_item_Name;
import static cheng.build.item.armor.BuildDriver.organicMatter_item_Name;

public class ClearDriver extends ABaseData {
    String
            organicMatter = organicMatter_item_Name,
            inorganicMatter = inorganicMatter_item_Name;
    public void clear(){
        PlayerBuildData data = DataManager.get(player);
        CompoundTag driverTag = data.getDriverTag();
        // 基础判断：玩家不为null 非客户端执行
        boolean baseBoolean = player != null && !player.level.isClientSide();

        if (!baseBoolean && !data.isEquieDriver()) return;

        if (data.isDriverInUse())return;
        if (driverTag.get(inorganicMatter) == null)return;
        if (driverTag.get(organicMatter) == null)return;

        if (player.isShiftKeyDown()) {

            if (!driverTag.getCompound(inorganicMatter).isEmpty()) {
                setTagAndTagItem(inorganicMatter);
                return;
            }
            if (!driverTag.getCompound(organicMatter).isEmpty()) {
                setTagAndTagItem(organicMatter);
                return;
            }

            if (data.isHenshin()) {
                ArmorUseHandler.loadArmor(player);
                data.setCurrentMode(PlayerBuildData.TransformMode.IDLE);
                return;
            }

            data.ClientMessage(PlayerBuildData.ClientMessageEnum.Air);
            data.setCurrentMode(PlayerBuildData.TransformMode.IDLE);

        }
        else if (!player.isShiftKeyDown()){
            if (!driverTag.getCompound(organicMatter).isEmpty()){
                setTagAndTagItem(organicMatter);
                return;
            }
            if (!driverTag.getCompound(inorganicMatter).isEmpty()) {
                setTagAndTagItem(inorganicMatter);
                return;
            }
            if (data.isHenshin()) {
                ArmorUseHandler.loadArmor(player);
                data.setCurrentMode(PlayerBuildData.TransformMode.IDLE);
                return;
            }
            data.ClientMessage(PlayerBuildData.ClientMessageEnum.Air);
            data.setCurrentMode(PlayerBuildData.TransformMode.IDLE);
        }
    }

    private void setTagAndTagItem(String itemTag){
        PlayerBuildData data = DataManager.get(player);
        CompoundTag driverTag = data.getDriverTag();
        ItemHelper.giveItem(player, BuildDriver.loadItem(Objects.requireNonNull(driverTag),itemTag));
        driverTag.putString(itemTag, "");
        player.getInventory().setChanged();
        player.inventoryMenu.broadcastChanges();
    }
}
