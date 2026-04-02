package cheng.build.init;

import cheng.build.item.bottle.bottle_effect.*;
import cheng.build.Build;
import cheng.cheng_util.ChengRegistriesUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class InitMobEffect {
    public static final DeferredRegister<MobEffect> register = ChengRegistriesUtil.deferredMobEffectRegister(Build.MODID);
    public static final RegistryObject<BottleMobEffect>
            rabbat_effect = registry("rabbat_bottle",()->new RabbatMobEffect(MobEffectCategory.NEUTRAL,-39271)),
            tank_effect = registry("tank_bottle",()->new TankMobEffect(MobEffectCategory.NEUTRAL,-13421569)),
            gorilla_effect = registry("gorilla_bottle",()->new GorillaMobEffect(MobEffectCategory.NEUTRAL,4866583)),
            diamond_effect = registry("diamond_bottle",()->new DiamondMobEffect(MobEffectCategory.NEUTRAL,13828095));
    private static RegistryObject<BottleMobEffect> registry(String name, Supplier<BottleMobEffect> bottleMobEffect){
        return ChengRegistriesUtil.registerMobEffect(register,name,bottleMobEffect);
    }
}
