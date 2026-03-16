package cheng.build.item.armor;

import cheng.build.item.armor.base.BuildArmor;
import cheng.build.init.InitSound;
import com.google.common.collect.Multimap;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.crafting.Ingredient;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;

public class BuildBaseArmor extends BuildArmor {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public BuildBaseArmor() {
        super(new easyArmor(0, 10,0,
                InitSound.build_driver_equie, Ingredient.EMPTY,"",
                5,0.2f), EquipmentSlot.FEET, new Properties());
    }

    @Override
    public void registerControllers(AnimationData data) {}

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public List<MobEffectInstance> MobEffectInstanceList() {
        return List.of(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,1,1,false,false,false));
    }
}
