package cheng.build.armor;

import cheng.build.ArmorUseHandler;
import cheng.build.ItemHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class BuildArmor extends ARMOR{
    private final List<MobEffectInstance> mobEffectInstanceList;
    public BuildArmor(ArmorMaterial armorMaterial, EquipmentSlot slot, Properties properties) {
        super(armorMaterial, slot, properties);
        this.mobEffectInstanceList = MobEffectInstanceList();
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        for (MobEffectInstance e : this.mobEffectInstanceList) {

            // 检查效果是否已存在
            MobEffectInstance existingEffect = player.getEffect(e.getEffect());
            if (existingEffect != null && existingEffect.getDuration() > 200) {
                continue; // 效果还在持续，不需要重新应用
            }

            // 应用效果
            player.addEffect(new MobEffectInstance(
                    e.getEffect(), 600, e.getAmplifier(), // 延长持续时间到30秒
                    e.isAmbient(), e.isVisible(), e.showIcon()));
        }
        Item
                head = player.getItemBySlot(EquipmentSlot.HEAD).getItem(),
                chest = player.getItemBySlot(EquipmentSlot.CHEST).getItem(),
                feet = player.getItemBySlot(EquipmentSlot.FEET).getItem();
        // 如果穿的三件套是创骑盔甲的子类 就返回（不清除盔甲）
        boolean isEquieBuildArmor = head instanceof BuildArmor && chest instanceof BuildArmor && feet instanceof BuildArmor;
        if (isEquieBuildArmor) return;

        ItemHelper.removeItem(player, this);
        if (player instanceof ServerPlayer serverPlayer)
            // 加载保存在玩家数据中的盔甲
            ArmorUseHandler.loadArmor(serverPlayer);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        if (pEntity instanceof Player player) {
            Item
                    head = player.getItemBySlot(EquipmentSlot.HEAD).getItem(),
                    chest = player.getItemBySlot(EquipmentSlot.CHEST).getItem(),
                    feet = player.getItemBySlot(EquipmentSlot.FEET).getItem();
            boolean isEquieBuildArmor = head instanceof BuildArmor && chest instanceof BuildArmor && feet instanceof BuildArmor;
            if (isEquieBuildArmor) return;

            ItemHelper.removeItem(player, this);
        }
    }

    public abstract List<MobEffectInstance> MobEffectInstanceList();
}
