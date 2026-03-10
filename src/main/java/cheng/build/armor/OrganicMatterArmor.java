package cheng.build.armor;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;

/**
 * 有机物盔甲
 */
public abstract class OrganicMatterArmor extends BuildArmor {
    public OrganicMatterArmor(ArmorMaterial armorMaterial, Properties properties) {
        super(armorMaterial, EquipmentSlot.HEAD, properties);
    }
}
