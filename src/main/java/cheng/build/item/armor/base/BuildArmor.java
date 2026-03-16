package cheng.build.item.armor.base;

import cheng.build.ArmorUseHandler;
import cheng.build.ItemHelper;
import cheng.build.item.armor.BuildBaseArmor;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

public abstract class BuildArmor extends ARMOR{
    private final List<MobEffectInstance> mobEffectInstanceList;
    public BuildArmor(ArmorMaterial armorMaterial, EquipmentSlot slot, Properties properties) {
        super(armorMaterial, slot, properties);
        this.mobEffectInstanceList = MobEffectInstanceList();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
        Multimap<Attribute, AttributeModifier> mo = ArrayListMultimap.create();
        // 添加原ArmorMaterial参数
        mo.putAll(super.getDefaultAttributeModifiers(pEquipmentSlot));
        // 添加重写后的属性修饰词
        Multimap<Attribute, AttributeModifier> multimap = AttributeModifiermap();
        switch (pEquipmentSlot){
            case HEAD -> {
                if (this instanceof OrganicMatterArmor) {
                    if (!multimap.isEmpty()) {
                        mo.putAll(AttributeModifiermap());
                    }
                }
            }
            case CHEST -> {
                if (this instanceof InorganicMatterArmor){
                    if (!multimap.isEmpty()) {
                        mo.putAll(AttributeModifiermap());
                    }
                }
            }
            case FEET -> {
                if (this instanceof BuildBaseArmor) {
                    if (!multimap.isEmpty()) {
                        mo.putAll(AttributeModifiermap());
                    }
                }
            }
        }
        return mo;
    }

    protected Multimap<Attribute, AttributeModifier> AttributeModifiermap() {
        return ArrayListMultimap.create();
    }

    private Player player;
    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        this.player = player;
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
        if (isEquieBuildArmor()) return;
        resetArmor();
        ItemHelper.removeItem(player, this);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        if (pEntity instanceof Player player) {
            this.player = player;
            if (isEquieBuildArmor()) return;
            resetArmor();
            ItemHelper.removeItem(player, this);
        }
    }

    public abstract List<MobEffectInstance> MobEffectInstanceList();
    // 判断是否穿着BuildArmor的子类盔甲
    private boolean isEquieBuildArmor(){
        Item
                head = player.getItemBySlot(EquipmentSlot.HEAD).getItem(),
                chest = player.getItemBySlot(EquipmentSlot.CHEST).getItem(),
                feet = player.getItemBySlot(EquipmentSlot.FEET).getItem();
        return head instanceof BuildArmor && chest instanceof BuildArmor && feet instanceof BuildArmor;
    }
    // 恢复盔甲
    private void resetArmor(){
        // 加载保存在玩家数据中的盔甲
        if (player instanceof ServerPlayer serverPlayer)
            ArmorUseHandler.loadArmor(serverPlayer);
    }
    protected AttributeModifier attributeModifier(String Armorname, String AttributeName, double pAmount, AttributeModifier.Operation operation){
        return new AttributeModifier(UUID.nameUUIDFromBytes(("Build"+Armorname+"Armor"+AttributeName).getBytes(StandardCharsets.UTF_8)).toString(),pAmount, operation);
    }
}
