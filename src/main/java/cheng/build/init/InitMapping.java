package cheng.build.init;

import cheng.build.Build;
import cheng.build.keybingds.ClearDriverKeybingd;
import cheng.build.keybingds.ShakeBottleMessageKey;
import cheng.build.keybingds.RotaryDriverMessageKey;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = {Dist.CLIENT}
)
public class InitMapping {
    private static final String Category = "key.categories.kamenrider_build";
    public static final CustomKeyMapping
            ShakeBottle = new CustomKeyMapping("key.kamenrider_build.shake_bottle", GLFW.GLFW_KEY_C, Category) {
        void onPress() {
            Build.PACKET_HANDLER.sendToServer(new ShakeBottleMessageKey(0, 0));
        }
        void onRelease() {
            Build.PACKET_HANDLER.sendToServer(new ShakeBottleMessageKey(1, 0));
        }
        void onHold() {}
    },
            RotaryDriver = new CustomKeyMapping("key.kamenrider_build.rotary_driver", GLFW.GLFW_KEY_R,Category){
        void onPress() {
            Build.PACKET_HANDLER.sendToServer(new RotaryDriverMessageKey(0, 0));
        }
        void onRelease() {
            Build.PACKET_HANDLER.sendToServer(new RotaryDriverMessageKey(1, 0));
        }
        void onHold() {
                }
                },
            ClearDriver = new CustomKeyMapping("key.kamenrider_build.clear_driver", GLFW.GLFW_KEY_X, Category) {
        void onPress() {
            Build.PACKET_HANDLER.sendToServer(new ClearDriverKeybingd(0, 0));
        }
        void onRelease() {
        }
        void onHold() {
        }
    };

    @SubscribeEvent
    public static void registerKeyBindings(FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(ShakeBottle);
        ClientRegistry.registerKeyBinding(RotaryDriver);
        ClientRegistry.registerKeyBinding(ClearDriver);
    }

    @Mod.EventBusSubscriber({Dist.CLIENT})
    public static class KeyEventListener {
        @SubscribeEvent
        public static void onKeyInput(TickEvent.ClientTickEvent event) {
            // 只在没有打开GUI时处理
            if (Minecraft.getInstance().screen != null) return;

            ShakeBottle.update();
            RotaryDriver.update();
            ClearDriver.update();
        }
    }
    public abstract static class CustomKeyMapping extends KeyMapping {
        private boolean wasPressed = false;

        public CustomKeyMapping(String name, int key, String category) {
            super(name, key, category);
        }

        public void update() {
            boolean isPressed = this.isDown();

            if (isPressed && !wasPressed) {
                onPress();      // 刚按下
            } else if (!isPressed && wasPressed) {
                onRelease();    // 刚松开
            } else if (isPressed && wasPressed) {
                onHold();       // 按住中（每 tick 触发）
            }
            wasPressed = isPressed;
        }

        abstract void onPress();
        abstract void onRelease();
        abstract void onHold();  // 新增：按住时每 tick 触发
    }
}
