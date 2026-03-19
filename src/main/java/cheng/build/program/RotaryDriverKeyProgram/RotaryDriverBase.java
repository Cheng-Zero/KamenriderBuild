package cheng.build.program.RotaryDriverKeyProgram;

import cheng.build.data.ABaseData;
import cheng.build.item.armor.BuildDriver;
import cheng.build.rider_syteam.HenshinUtil;
import net.minecraft.world.item.ItemStack;

public abstract class RotaryDriverBase extends ABaseData {
    // 常量定义
    protected static final int HENSHIN_DELAY = 40;
    protected static final int EFFECT_DELAY = 60;

    protected boolean isDriverInUse() {
        return driverTag.getBoolean("isUse");
    }

    protected void setDriverInUse(boolean inUse) {
        driverTag.putBoolean("isUse", inUse);
    }

    protected boolean hasBothBottles() {
        ItemStack organic = loadItem(driverTag,BuildDriver.organicMatter_item_Name);
        ItemStack inorganic = loadItem(driverTag,BuildDriver.inorganicMatter_item_Name);
        return !organic.isEmpty() && !inorganic.isEmpty();
    }

    protected HenshinUtil createHenshinUtil() {
        HenshinUtil util = new HenshinUtil();
        util.update(player);
        return util;
    }
}
