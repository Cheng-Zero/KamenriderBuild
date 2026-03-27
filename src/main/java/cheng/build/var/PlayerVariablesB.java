package cheng.build.var;

import net.minecraft.nbt.CompoundTag;

public interface PlayerVariablesB {
    CompoundTag writeNBT();
    void readNBT(CompoundTag Tag);
}
