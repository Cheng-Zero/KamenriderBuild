package cheng.build.KeyProgram.ClearKeyProgram;

import cheng.build.ArmorUseHandler;
import cheng.build.ItemHelper;
import cheng.build.item.armor.BuildDriver;
import cheng.build.data.ABaseData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

import static cheng.build.item.armor.BuildDriver.inorganicMatter_item_Name;
import static cheng.build.item.armor.BuildDriver.organicMatter_item_Name;

public class ClearDriver extends ABaseData {
    private boolean isUse;

    @Override
    public void update(Player player) {
        super.update(player);
        if (player!= null){
            isUse = driverTag.getBoolean("isUse");
        }else {
            isUse = true;
        }
    }

    public void clear(){
        if (!baseBoolean() && !equieDriver) return;

        if (isUse)return;
        if (driverTag.get(inorganicMatter_item_Name) == null)return;
        if (driverTag.get(organicMatter_item_Name) == null)return;

        if (player.isShiftKeyDown()) {

            if (!driverTag.getCompound(inorganicMatter_item_Name).isEmpty()){
                setTagAndTagItem(inorganicMatter_item_Name);
            }else if (!driverTag.getCompound(organicMatter_item_Name).isEmpty()) {
                setTagAndTagItem(organicMatter_item_Name);
            }else if (isHenshin()) {
                if (player instanceof ServerPlayer serverPlayer)
                 ArmorUseHandler.loadArmor(serverPlayer);
            }else
                ClientMessage(ClientMessageEnum.Air);
        }
        else if (!player.isShiftKeyDown()){
            if (!driverTag.getCompound(organicMatter_item_Name).isEmpty()){
                setTagAndTagItem(organicMatter_item_Name);
            }else if (!driverTag.getCompound(inorganicMatter_item_Name).isEmpty()) {
                setTagAndTagItem(inorganicMatter_item_Name);
            }else if (isHenshin()) {
                if (player instanceof ServerPlayer serverPlayer)
                    ArmorUseHandler.loadArmor(serverPlayer);
            }else
                ClientMessage(ClientMessageEnum.Air);
        }
    }

    private void setTagAndTagItem(String itemTag){
        ItemHelper.giveItem(player, BuildDriver.loadItem(Objects.requireNonNull(driverTag),itemTag));
        ClearTag(itemTag);
    }

    /// 基础判断：玩家不为null 非客户端执行
    private boolean baseBoolean(){
        return player != null && !player.level.isClientSide();
    }

    private void ClearTag(String tagName){
        driverTag.putString(tagName, "");
        player.getInventory().setChanged();
        player.inventoryMenu.broadcastChanges();
    }
}
