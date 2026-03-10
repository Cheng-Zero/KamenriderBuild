package cheng.build.armor;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
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
    }

    public abstract List<MobEffectInstance> MobEffectInstanceList();
}
