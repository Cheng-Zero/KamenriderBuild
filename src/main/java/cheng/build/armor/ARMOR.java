package cheng.build.armor;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.item.GeoArmorItem;

public abstract class ARMOR extends GeoArmorItem implements IAnimatable {
    public ARMOR(ArmorMaterial materialIn, EquipmentSlot slot, Properties builder) {
        super(materialIn, slot, builder);
    }

    public static class easyArmor implements ArmorMaterial{
        private static EquipmentSlot equipmentSlot;
        private final int DurabilityForSlot,DefenseForSlot,EnchantmentValue;
        private final RegistryObject<SoundEvent> EquipSound;
        private final Ingredient RepairIngredient;
        private final String Name;
        private final float Toughness,KnockbackResistance;
        /**
         * 盔甲数值构造函数
         * @param durabilityForSlot 耐久
         * @param defenseForSlot 护甲值
         * @param enchantmentValue 附魔等级
         * @param equipSound 装配音效
         * @param repairIngredient 修补物品
         * @param name 物品名称
         * @param toughness 盔甲韧性
         * @param knockbackResistance 击退抗性
         */
        public easyArmor(int durabilityForSlot,
                         int defenseForSlot,
                         int enchantmentValue,
                         RegistryObject<SoundEvent> equipSound,
                         Ingredient repairIngredient,
                         String name,
                         float toughness,
                         float knockbackResistance) {
            this.DurabilityForSlot = durabilityForSlot;
            this.DefenseForSlot = defenseForSlot;
            this.EnchantmentValue = enchantmentValue;
            this.EquipSound = equipSound;
            this.RepairIngredient = repairIngredient;
            this.Name = name;
            this.Toughness = toughness;
            this.KnockbackResistance = knockbackResistance;
        }
        protected static int ForSlot(int head,int chest,int leg,int boots){
            switch (equipmentSlot){
                case HEAD -> {return head;}
                case CHEST -> {return chest;}
                case LEGS -> {return leg;}
                case FEET -> {return boots;}
            }
            return 0;
        }
        protected static float ForSlot(float head,float chest,float leg,float boots){
            switch (equipmentSlot){
                case HEAD -> {return head;}
                case CHEST -> {return chest;}
                case LEGS -> {return leg;}
                case FEET -> {return boots;}
            }
            return 0f;
        }
        public int getDurabilityForSlot(@NotNull EquipmentSlot pSlot) {
            return this.DurabilityForSlot;
        }
        public int getDefenseForSlot(@NotNull EquipmentSlot pSlot) {
            return this.DefenseForSlot;
        }
        public int getEnchantmentValue() {
            return this.EnchantmentValue;
        }
        public @NotNull SoundEvent getEquipSound() {
            return this.EquipSound.get();
        }
        public @NotNull Ingredient getRepairIngredient() {
            return this.RepairIngredient;
        }
        public @NotNull String getName() {
            return this.Name;
        }
        public float getToughness() {
            return this.Toughness;
        }
        public float getKnockbackResistance() {
            return this.KnockbackResistance;
        }
        // 没用的东西
        private void setEquipmentSlot(EquipmentSlot equipmentSlot) {
            easyArmor.equipmentSlot = equipmentSlot;
        }
    }
}
