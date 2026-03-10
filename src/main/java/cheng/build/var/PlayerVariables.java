package cheng.build.var;

import cheng.build.Build;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.PacketDistributor;

/**
 * 这添加后需到
 */
public class PlayerVariables {
	public byte hazard_level = 1;

	public void syncPlayerVariables(Entity entity) {
		if (entity instanceof ServerPlayer serverPlayer)
			Build.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new PlayerVariablesSyncMessage(this));
	}

	public Tag writeNBT() {
		CompoundTag nbt = new CompoundTag();
		nbt.putByte("hazard_level", hazard_level);
		return nbt;
	}

	public void readNBT(Tag Tag) {
		CompoundTag nbt = (CompoundTag) Tag;
		hazard_level = nbt.getByte("hazard_level");
	}
}