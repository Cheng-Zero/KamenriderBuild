package cheng.build.rider_syteam;

import cheng.build.api.ISkill;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AbilityManager {

	// 存储每个玩家的当前能力索引（临时状态）
	private static final Map<UUID, Integer> playerAbilityIndexes = new HashMap<>();

	public static void execute(LivingEntity entity) {
		if (entity == null || entity.level.isClientSide) return;

		if (entity instanceof ServerPlayer serverPlayer) {
			PlayerDataManager data = PlayerDataManager.get(serverPlayer);
			if (data == null) return;

			// 判断是否为变身状态
			if(data.getCurrentState() != HenshinState.ACTIVE &&
					data.getCurrentState() != HenshinState.ACTIVE_AWAKENING) {
				return;
			}

			// 获取当前形态配置 - 使用您提供的getActiveFormConfig方法
			FormConfig form = getActiveFormConfig(data);
			if (form == null) return;

			// 获取能力列表
			List<ISkill> abilities = form.getAbilities();
			if (abilities.isEmpty()) return;

			// 获取或初始化能力索引
			UUID playerId = serverPlayer.getUUID();
			int currentIndex = playerAbilityIndexes.getOrDefault(playerId, 0);

			if (entity.isCrouching()) {
				// 蹲下状态：切换能力
				if (abilities.size() > 1) {
					currentIndex = (currentIndex + 1) % abilities.size();
					playerAbilityIndexes.put(playerId, currentIndex);

					// 获取当前能力
					IRiderSkill currentAbility = abilities.get(currentIndex);

					// 提示当前能力
					HenshinUtil.sendMessage(serverPlayer,
							"当前能力：" + currentAbility.getDisplayName() ,
							ChatFormatting.RED);
			}
			} else {
				// 非蹲下状态：执行当前能力
				ISkill ability = abilities.get(currentIndex);
				if (ability.isAvailable(serverPlayer)) {
					ability.skill(serverPlayer);
				}
			}
		}
	}

	// 使用您提供的getActiveFormConfig方法
	private static FormConfig getActiveFormConfig(PlayerDataManager data) {
		RiderConfig cfg = RiderRegistry.getConfig(data.getActiveRiderName());
		return cfg == null ? null : cfg.getForm(data.getActiveFormName());
	}
}