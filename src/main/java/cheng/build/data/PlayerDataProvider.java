package cheng.build.data;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerDataProvider implements ICapabilitySerializable<CompoundTag> {
    
    // 定义 Capability
    public static final Capability<IBuildCapability> PLAYER_DATA =
        CapabilityManager.get(new CapabilityToken<>() {});
    
    private final IBuildCapability data;
    private final LazyOptional<IBuildCapability> holder;
    
    public PlayerDataProvider(Player player) {
        this.data = new PlayerDataImpl(player);
        this.holder = LazyOptional.of(() -> data);
    }
    
    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == PLAYER_DATA ? holder.cast() : LazyOptional.empty();
    }
    
    @Override
    public CompoundTag serializeNBT() {
        return data.serializeNBT();
    }
    
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        data.deserializeNBT(nbt);
    }
}