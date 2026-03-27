package cheng.build.var;

import cheng.build.Build;
import cheng.build.message.IMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static cheng.build.var.ModVariables.PLAYER_VARIABLES_CAPABILITY;

public class PlayerVariablesMessage {
	public final PlayerVariables data;

	public PlayerVariablesMessage(PlayerVariables data) {
		if (data == null) {
			throw new IllegalArgumentException("玩家变量不能为空");
		}
		this.data = data;
	}

	public PlayerVariablesMessage(FriendlyByteBuf buffer) {
		CompoundTag nbt = buffer.readNbt();
		if(nbt == null) {
			throw new IllegalArgumentException("玩家NBT不能为空");
		}
		this.data = new PlayerVariables();
		this.data.readNBT(nbt);
	}

	public static void buffer(PlayerVariablesMessage p, FriendlyByteBuf buf) {
		try {
			buf.writeNbt(p.data.writeNBT());
		} catch (Exception e) {
			Build.LOGGER.error("编码PlayerVariablesMessage失败: {}", e.getMessage());
			throw new RuntimeException("编码PlayerVariablesMessage失败", e);
		}
	}

	public static void handler(PlayerVariablesMessage p, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		context.enqueueWork(() -> {
			if (!context.getDirection().getReceptionSide().isServer()) {
				PlayerVariables variables = (Minecraft.getInstance().player.getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()));
				variables.hazard_level = p.data.hazard_level;
			}
		});
		context.setPacketHandled(true);
	}
}