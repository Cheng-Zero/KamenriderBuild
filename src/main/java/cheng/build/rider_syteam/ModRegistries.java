package cheng.build.rider_syteam;

import cheng.build.Build;
import cheng.build.init.InitItem;
import cheng.build.init.InitMobEffect;
import cheng.build.init.InitSound;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Build.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRegistries {
    @SubscribeEvent
    public static void BottleRegistries(FMLCommonSetupEvent event) {
        event.enqueueWork(()->{
            BuildRegistry.register("Rabbat", InitItem.rabbat.get(), InitMobEffect.rabbat_effect.get(), InitSound.rabbat.get(), InitItem.buildRabbatArmor.get());
            BuildRegistry.register("Tank", InitItem.tank.get(), InitMobEffect.tank_effect.get(), InitSound.tank.get(), InitItem.buildTankArmor.get());
            BuildRegistry.register("Gorilla", InitItem.gorilla.get(), InitMobEffect.gorilla_effect.get(), InitSound.gorilla.get(), InitItem.buildGorillaArmor.get());
            BuildRegistry.register("Diamond", InitItem.diamond.get(), InitMobEffect.diamond_effect.get(), InitSound.diamond.get(), InitItem.buildDiamondArmor.get());

            BuildRegistry.register("RabbatTank",InitItem.rabbat.get(),InitItem.tank.get(),InitSound.best_match_rabbat_tank.get());
            BuildRegistry.register("GorillaDiamond",InitItem.gorilla.get(),InitItem.diamond.get(),InitSound.best_match_gorilla_diamond.get());
        });
    }
}
