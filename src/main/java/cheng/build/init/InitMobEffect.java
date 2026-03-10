package cheng.build.init;

import cheng.build.bottle.bottle_effect.BottleMobEffect;
import cheng.build.Build;
import cheng.build.bottle.bottle_effect.RabbatMobEffect;
import cheng.build.bottle.bottle_effect.TankMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitMobEffect {
    public static final DeferredRegister<MobEffect> register = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Build.MODID);
    public static final RegistryObject<BottleMobEffect> rabbat_effect = register.register("rabbat_bottle",()->new RabbatMobEffect(MobEffectCategory.NEUTRAL,-39271));
    public static final RegistryObject<BottleMobEffect> tank_effect = register.register("tank_bottle",()->new TankMobEffect(MobEffectCategory.NEUTRAL,-13421569));
}
