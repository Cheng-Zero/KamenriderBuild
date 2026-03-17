package cheng.build.client.hud;

import cheng.build.api.IFullBottle;
import cheng.build.bottle.BottleRegistry;
import cheng.build.init.InitItem;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class BuildDriverFullBottleHolder extends Screen {
    public static BuildDriverFullBottleHolder instance;
    public boolean active = false;
    private final List<IFullBottle> bottles;
    private int selectedIndex = 0;
    private int scrollOffset = 0;
    private static final int VISIBLE_SLOTS = 5;

    // 鼠标位置跟踪
    private int mouseX = 0;
    private int mouseY = 0;

    public BuildDriverFullBottleHolder() {
        super(new TranslatableComponent("narration.button"));
        this.bottles = BottleRegistry.getAllBottles();
    }

    public static BuildDriverFullBottleHolder getInstance() {
        if (instance == null) {
            instance = new BuildDriverFullBottleHolder();
        }
        return instance;
    }
    public void open() {
        Minecraft.getInstance().submit(()->{
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            if (player != null) {
                this.active = true;
                mc.setScreen(this);
                mc.mouseHandler.releaseMouse();
            }
        });
    }

    @Override
    public boolean isPauseScreen() {
        return false; // 游戏不暂停
    }
}
