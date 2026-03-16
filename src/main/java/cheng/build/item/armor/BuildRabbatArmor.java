package cheng.build.item.armor;

import cheng.build.item.armor.base.OrganicMatterArmor;
import cheng.build.init.InitSound;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.crafting.Ingredient;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;

public class BuildRabbatArmor extends OrganicMatterArmor {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public BuildRabbatArmor() {
        super(new easyArmor(0, 10,0,
                InitSound.build_driver_equie, Ingredient.EMPTY,"",
                5,0.2f), new Properties());
    }

    @Override
    public void registerControllers(AnimationData data) {}

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public List<MobEffectInstance> MobEffectInstanceList() {
        return List.of();
    }
}
