package cheng.build.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IBuildCapability extends INBTSerializable<CompoundTag> {
    /**
     * 获取数据
     */
    PlayerBuildData getData();
    /**
     * 设置数据
     */
    void setData(PlayerBuildData data);
    /**
     * 复制
     */
    void copyFrom(IBuildCapability source);
}
