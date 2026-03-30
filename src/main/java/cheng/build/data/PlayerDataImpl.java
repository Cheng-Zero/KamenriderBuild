package cheng.build.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class PlayerDataImpl implements IBuildCapability {
    
    private PlayerBuildData data;
    
    public PlayerDataImpl(Player player) {
        this.data = new PlayerBuildData(player);
    }
    
    @Override
    public PlayerBuildData getData() { return data; }
    
    @Override
    public void setData(PlayerBuildData data) { this.data = data; }
    
    @Override
    public void copyFrom(IBuildCapability source) { this.data = source.getData(); }
    
    @Override
    public CompoundTag serializeNBT() { return data.save(); }
    
    @Override
    public void deserializeNBT(CompoundTag nbt) { data.load(nbt); }
}