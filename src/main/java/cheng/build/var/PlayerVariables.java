package cheng.build.var;

import cheng.build.Build;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.PacketDistributor;

import java.util.Random;

/**
 * 这添加后需到{@link PlayerVariablesMessage}添加
 */
public class PlayerVariables implements PlayerVariablesB{
	static Random random = new Random();
	public double hazard_level = HazardLevel();

	public void syncPlayerVariables(Entity entity) {
		if (entity instanceof ServerPlayer serverPlayer)
			Build.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new PlayerVariablesMessage(this));
	}

	public CompoundTag writeNBT() {
		CompoundTag nbt = new CompoundTag();
		nbt.putDouble("hazard_level", hazard_level);
		return nbt;
	}

	public void readNBT(CompoundTag Tag) {
        hazard_level = Tag.getDouble("hazard_level");
	}

	/**
	 * 初始危险等级计算
	 */
	double HazardLevel() {
		while (true) {
			// 0.0 - 1.0
			double finalValue = random.nextDouble() * 10;
			// 值 大于1.0 同时 小于3.0 时 返回值
			if (finalValue <= 3 && finalValue > 1) {
				// 只取1位小数
                return Math.round(finalValue);
			}
		}
	}
}