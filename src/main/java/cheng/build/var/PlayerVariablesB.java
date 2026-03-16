package cheng.build.var;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public abstract class PlayerVariablesB {
    public abstract Tag writeNBT();
    public abstract void readNBT(Tag Tag);
}
