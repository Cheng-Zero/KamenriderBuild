package cheng.build.init;

import cheng.build.Build;
import cheng.build.client.hud.BuildDriverFullBottleHolder;
import cheng.build.keybingds.ClearDriverKeybingd;
import cheng.build.keybingds.GetHolderKeybingd;
import cheng.build.keybingds.ShakeBottleMessageKey;
import cheng.build.keybingds.RotaryDriverMessageKey;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = {Dist.CLIENT}
)
public class InitMapping {
    public static final KeyMapping
            ShakeBottle = new KeyMapping("key.kamenrider_build.shake_bottle", GLFW.GLFW_KEY_C,"key.categories.kamenrider_build"),
            RotaryDriver = new KeyMapping("key.kamenrider_build.rotary_driver", GLFW.GLFW_KEY_R,"key.categories.kamenrider_build"),
            ClearDriver = new KeyMapping("key.kamenrider_build.clear_driver", GLFW.GLFW_KEY_X,"key.categories.kamenrider_build"),
            GetHolder = new KeyMapping("key.kamenrider_build.get_holder", GLFW.GLFW_KEY_Z,"key.categories.kamenrider_build")
                    ;

    @SubscribeEvent
    public static void registerKeyBindings(FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(ShakeBottle);
        ClientRegistry.registerKeyBinding(RotaryDriver);
        ClientRegistry.registerKeyBinding(ClearDriver);
        ClientRegistry.registerKeyBinding(GetHolder);
    }

    @Mod.EventBusSubscriber({Dist.CLIENT})
    public static class KeyEventListener {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.KeyInputEvent event) {
            // 只在没有打开GUI时处理
            if (Minecraft.getInstance().screen != null) return;

            VortexLever(event);
            ShakeBottle(event);
            ClearDriver(event);
            GetHolder(event);
        }
        private static void VortexLever(InputEvent.KeyInputEvent event) {
            if (event.getKey() != RotaryDriver.getKey().getValue()) return;
            if (event.getAction() == GLFW.GLFW_PRESS) {
                Build.PACKET_HANDLER.sendToServer(new RotaryDriverMessageKey(0, 0));
            } else if (event.getAction() == GLFW.GLFW_RELEASE) {
                Build.PACKET_HANDLER.sendToServer(new RotaryDriverMessageKey(1, 0));
            }
        }
        private static void ShakeBottle(InputEvent.KeyInputEvent event) {
            if (event.getKey() != ShakeBottle.getKey().getValue()) return;
            if (event.getAction() == GLFW.GLFW_PRESS) {
                Build.PACKET_HANDLER.sendToServer(new ShakeBottleMessageKey(0, 0));
            } else if (event.getAction() == GLFW.GLFW_RELEASE) {
                Build.PACKET_HANDLER.sendToServer(new ShakeBottleMessageKey(1, 0));
            }
        }
        private static void ClearDriver(InputEvent.KeyInputEvent event) {
            if (event.getKey() != ClearDriver.getKey().getValue()) return;
            if (event.getAction() == GLFW.GLFW_PRESS) {
                Build.PACKET_HANDLER.sendToServer(new ClearDriverKeybingd(0, 0));
            }
        }
        private static void GetHolder(InputEvent.KeyInputEvent event) {
            if (event.getKey() != GetHolder.getKey().getValue()) return;
            if (event.getAction() == GLFW.GLFW_PRESS) {
                Build.PACKET_HANDLER.sendToServer(new GetHolderKeybingd(0, 0));
            } else if (event.getAction() == GLFW.GLFW_RELEASE) {
                Build.PACKET_HANDLER.sendToServer(new GetHolderKeybingd(1, 0));
            }
        }
    }
}
