package cheng.build.data;

import cheng.build.Build;
import cheng.build.api.IFullBottle;
import cheng.build.item.armor.BuildDriver;
import cheng.build.rider_syteam.BuildRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ABaseData {
    protected Player player;

    // 更新数据
    public void update(Player player) {
        if (player == null) {
            return;
        }
        this.player = player;
    }

    protected ItemStack loadItem(CompoundTag parent, String key) {
        return parent.contains(key) ? ItemStack.of(parent.getCompound(key)) : ItemStack.EMPTY;
    }

    public boolean CurrentFormNotBottle(ItemStack itemStack, ItemStack item){
        Build.LOGGER.debug("Inorganic Item：{}",itemStack);
        Build.LOGGER.debug("Organic Item：{}",item);
        IFullBottle byItemStack = BuildRegistry.FullBottlefindByItemStack(itemStack);
        IFullBottle byItem = BuildRegistry.findByArmorItem(item.getItem());
        Build.LOGGER.debug("IFullBottle Inorganic：{}",byItemStack);
        Build.LOGGER.debug("IFullBottle Organic：{}",byItem);
        // 如果瓶子不存在，或者瓶子与当前装备的护甲不匹配
        return byItemStack == null || byItem == null || !byItemStack.equals(byItem);
    }
}
