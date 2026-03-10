package cheng.build.armor;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;

/**
 * 无机物盔甲
 */
public abstract class InorganicMatterArmor extends BuildArmor{
    public InorganicMatterArmor(ArmorMaterial armorMaterial,Properties properties) {
        super(armorMaterial, EquipmentSlot.CHEST, properties);
    }
}
