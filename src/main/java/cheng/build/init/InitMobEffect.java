package cheng.build.init;

import cheng.build.item.bottle.bottle_effect.*;
import cheng.build.Build;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitMobEffect {
    public static final DeferredRegister<MobEffect> register = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Build.MODID);
    public static final RegistryObject<BottleMobEffect>
            rabbat_effect = register.register("rabbat_bottle",()->new RabbatMobEffect(MobEffectCategory.NEUTRAL,-39271)),
            tank_effect = register.register("tank_bottle",()->new TankMobEffect(MobEffectCategory.NEUTRAL,-13421569)),
            gorilla_effect = register.register("gorilla_bottle",()->new GorillaMobEffect(MobEffectCategory.NEUTRAL,4866583)),
            diamond_effect = register.register("diamond_bottle",()->new DiamondMobEffect(MobEffectCategory.NEUTRAL,13828095));
}
