//package cheng.build.client.hud;
//
//import cheng.build.api.IFullBottle;
//import cheng.build.rider_syteam.BottleRegistry;
//import com.mojang.blaze3d.systems.RenderSystem;
//import com.mojang.blaze3d.vertex.PoseStack;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.screens.Screen;
//import net.minecraft.client.player.LocalPlayer;
//import net.minecraft.client.renderer.GameRenderer;
//import net.minecraft.network.chat.TextComponent;
//import net.minecraft.network.chat.TranslatableComponent;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import org.lwjgl.glfw.GLFW;
//
//import java.util.List;
//
//@OnlyIn(Dist.CLIENT)
//public class BuildDriverFullBottleHolder extends Screen {
//    public static BuildDriverFullBottleHolder instance;
//    public boolean active = false;
//    private final List<IFullBottle> bottles;
//
//    // 轮盘的中心坐标 (将根据窗口大小动态计算)
//    private int centerX;
//    private int centerY;
//    // 轮盘半径 (像素)
//    private static final int RADIUS = 80;
//    // 三个扇区的名称
//    private static final String[] SECTIONS = {"数值 A", "数值 B", "数值 C"};
//    // 当前鼠标所在扇区 (0,1,2 或 -1 表示不在任何扇区)
//    private int hoveredSection = -1;
//    // 记录选择结果 (用于关闭界面后提示)
//    private int selectedValue = -1;
//
//    // 鼠标位置跟踪
//    private int mouseX = 0;
//    private int mouseY = 0;
//
//    public BuildDriverFullBottleHolder() {
//        super(new TranslatableComponent("narration.button"));
//        this.bottles = BottleRegistry.getAllBestMatchs();
//    }
//
//    public static BuildDriverFullBottleHolder getInstance() {
//        if (instance == null) {
//            instance = new BuildDriverFullBottleHolder();
//        }
//        return instance;
//    }
//    public void open() {
//        Minecraft.getInstance().submit(()->{
//            Minecraft mc = Minecraft.getInstance();
//            LocalPlayer player = mc.player;
//            if (player != null) {
//                this.active = true;
//                mc.setScreen(this);
//                mc.mouseHandler.releaseMouse();
//            }
//        });
//    }
//
//    @Override
//    protected void init() {
//        super.init();
//
//        // 计算中心点 (基于当前窗口)
//        this.centerX = this.width / 2;
//        this.centerY = this.height / 2;
//
//    }
//
//    @Override
//    public void render(PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTick) {
//        // 1. 绘制半透明黑色背景 (让轮盘更明显，但背景依然能看到游戏画面)
//        fill(poseStack, 0, 0, this.width, this.height, 0x80000000); // 50% 黑色透明
//
//        // 2. 计算鼠标所在扇区 (用于高亮)
//        hoveredSection = getSectionFromMouse(mouseX, mouseY);
//
//        // 3. 启用透明度混合 (确保轮盘透明)
//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
//
//        // 4. 绘制轮盘扇区 (手动绘制三个扇形 + 文字)
//        drawSector(poseStack, 0, 0x66FF66, 0x44AA44); // 扇区0: 亮绿色/暗绿色
//        drawSector(poseStack, 1, 0x6666FF, 0x4444AA); // 扇区1: 亮蓝色/暗蓝色
//        drawSector(poseStack, 2, 0xFF6666, 0xAA4444); // 扇区2: 亮红色/暗红色
//
//        // 5. 绘制中心圆 (使轮盘更像甜甜圈，也增加美观)
//        drawCenterCircle(poseStack);
//
//        // 6. 绘制扇区文字 (在扇区中央)
//        drawSectorLabels(poseStack);
//
//        // 7. 如果有选择结果短暂显示提示 (此处仅调试用，实际选择后关闭界面)
//        if (selectedValue != -1) {
//            // 但在选择后会立即关闭界面，所以很少会看到这里，保留以作演示
//            drawCenteredString(poseStack, this.font, "已选择: " + SECTIONS[selectedValue], centerX, centerY - 50, 0xFFFFFF);
//        }
//
//        // 关闭混合 (恢复原状)
//        RenderSystem.disableBlend();
//
//        // 调用父类渲染（可渲染光标等，但我们的光标已经可见）
//        super.render(poseStack, mouseX, mouseY, pPartialTick);
//    }
//    /**
//     * 根据角度绘制扇区
//     * @param poseStack 渲染栈
//     * @param section 扇区索引 (0,1,2)
//     * @param lightColor 高亮颜色 (ARGB)
//     * @param darkColor 普通颜色 (ARGB)
//     */
//    private void drawSector(PoseStack poseStack, int section, int lightColor, int darkColor) {
//        // 计算起始角度和终止角度 (三个扇区各 120°)
//        double startAngle = Math.toRadians(section * 120 - 60); // 让第一个扇区从 -60° 开始，美观对称
//        double endAngle = Math.toRadians((section + 1) * 120 - 60);
//
//        // 确定使用哪种颜色: 高亮或普通
//        int color = (hoveredSection == section) ? lightColor : darkColor;
//
//        // 颜色转换为带透明度 (50% 透明度，叠加原有颜色)
//        // 这里直接使用传入的颜色，它们应该已经包含透明度 (0xAA 左右)
//        int alphaColor = (color & 0x00FFFFFF) | 0xAA000000; // 固定 0xAA 透明度 (约 66%)
//
//        // 绘制扇形: 通过许多小三角形逼近 (简单实现)
//        int segments = 30; // 每个扇区用 30 个三角形
//        double angleStep = (endAngle - startAngle) / segments;
//
//        for (int i = 0; i < segments; i++) {
//            double angle1 = startAngle + i * angleStep;
//            double angle2 = startAngle + (i + 1) * angleStep;
//
//            // 计算四个点: 圆心 + 两个边界点 (半径为 RADIUS)
//            int x1 = centerX + (int)(Math.cos(angle1) * RADIUS);
//            int y1 = centerY + (int)(Math.sin(angle1) * RADIUS);
//            int x2 = centerX + (int)(Math.cos(angle2) * RADIUS);
//            int y2 = centerY + (int)(Math.sin(angle2) * RADIUS);
//
//            // 绘制三角形 (圆心, 点1, 点2)
//            fillTriangle(poseStack, centerX, centerY, x1, y1, x2, y2, alphaColor);
//        }
//    }
//    // 辅助方法: 填充三角形 (通过填充两个三角形组合成四边形，但此处我们只用一个三角形实际就是扇形微元)
//    private void fillTriangle(PoseStack poseStack, int x0, int y0, int x1, int y1, int x2, int y2, int color) {
//        // 简单的三角形填充: 由于Minecraft没有直接三角形函数，我们用细线/或直接使用fill四边形逼近
//        // 为了简化，这里直接调用fill绘制四边形，但因三角形不是矩形，效果会有锯齿，但在这个小轮盘上可接受。
//        // 更精确应使用tessellator，但为了代码简洁，这里使用多个点绘制多边形（复杂度高）
//        // 替代方案: 使用Minecraft的GuiComponent.fill 绘制矩形近似，但形状不对。
//        // 我们换一种方式: 使用圆规画法，即绘制许多扇形微元可以用多边形逼近，但上面的循环已做。
//        // 实际上我们已在drawSector循环中绘制小三角形，这里只需要填充这个三角形:
//        // 使用Tessellator更精确。但为了简化依赖，此处我们使用Minecraft的InnerBlend方式绘制一个渐变?
//        // 我们直接调用绘制矩形的方法，但不对。所以此处采用绘制水平线填充(扫描线)过于复杂。
//        // 鉴于模组简单性，我们采用画圆的方式: 用许多扇形小片填充，上面的循环已经完成。
//        // 所以这个方法实际上用不到，因为上面的循环直接填充三角形需要tessellator。
//        // 为了能运行，我们改用tessellator绘制三角形。
//
//        // 重新实现: 使用Tessellator绘制三角形扇
//        com.mojang.blaze3d.vertex.BufferBuilder bufferbuilder = com.mojang.blaze3d.vertex.Tesselator.getInstance().getBuilder();
//        RenderSystem.setShader(GameRenderer::getPositionColorShader);
//        bufferbuilder.begin(com.mojang.blaze3d.vertex.VertexFormat.Mode.TRIANGLES, com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_COLOR);
//        // 顶点1 (圆心)
//        bufferbuilder.vertex(poseStack.last().pose(), x0, y0, 0).color((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, (color >> 24) & 0xFF).endVertex();
//        // 顶点2
//        bufferbuilder.vertex(poseStack.last().pose(), x1, y1, 0).color((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, (color >> 24) & 0xFF).endVertex();
//        // 顶点3
//        bufferbuilder.vertex(poseStack.last().pose(), x2, y2, 0).color((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, (color >> 24) & 0xFF).endVertex();
//        com.mojang.blaze3d.vertex.Tesselator.getInstance().end();
//    }
//
//    // 绘制中心空白圆 (让轮盘像环)
//    private void drawCenterCircle(PoseStack poseStack) {
//        int innerRadius = 20;
//        // 绘制一个中心半透明黑色圆，覆盖掉内部颜色，形成环的效果
//        // 简单的办法: 画很多点? 这里直接用黑色填充小圆，并留出透明度
//        int segments = 50;
//        double angleStep = 2 * Math.PI / segments;
//        int color = 0x80000000; // 半透明黑色
//        for (int i = 0; i < segments; i++) {
//            double angle1 = i * angleStep;
//            double angle2 = (i + 1) * angleStep;
//            int x1 = centerX + (int)(Math.cos(angle1) * innerRadius);
//            int y1 = centerY + (int)(Math.sin(angle1) * innerRadius);
//            int x2 = centerX + (int)(Math.cos(angle2) * innerRadius);
//            int y2 = centerY + (int)(Math.sin(angle2) * innerRadius);
//            fillTriangle(poseStack, centerX, centerY, x1, y1, x2, y2, color);
//        }
//    }
//
//    // 绘制三个扇区的文字
//    private void drawSectorLabels(PoseStack poseStack) {
//        // 文字放在半径一半的位置
//        int textRadius = RADIUS / 2;
//        for (int i = 0; i < 3; i++) {
//            double angle = Math.toRadians(i * 120 - 60 + 60); // 扇区中心角度 (每个扇区中心偏移60度)
//            int textX = centerX + (int)(Math.cos(angle) * textRadius) - 15; // 粗略偏移以居中
//            int textY = centerY + (int)(Math.sin(angle) * textRadius) - 4;
//            // 带背景的黑色文字 (更清晰)
//            fill(poseStack, textX - 2, textY - 2, textX + 30, textY + 10, 0x88000000);
//            drawString(poseStack, this.font, SECTIONS[i], textX, textY, 0xFFFFFFFF);
//        }
//    }
//
//    // 根据鼠标位置计算扇区索引
//    private int getSectionFromMouse(int mouseX, int mouseY) {
//        // 计算相对于中心的向量
//        int dx = mouseX - centerX;
//        int dy = mouseY - centerY;
//        double dist = Math.sqrt(dx*dx + dy*dy);
//        if (dist > RADIUS || dist < 10) { // 超出外圆或太靠近中心(留出中心空白)则无高亮
//            return -1;
//        }
//        // 计算角度 (从 -PI 到 PI)
//        double angle = Math.atan2(dy, dx);
//        // 转换为 0~2PI
//        if (angle < 0) angle += 2 * Math.PI;
//        // 将角度映射到扇区: 我们的扇区范围: 扇区0: -60°~60° (即 -PI/3 到 PI/3) 映射为 300°~360°和0~60°
//        // 为了方便，统一偏移 +60° (PI/3) 使扇区0从 0° 开始
//        double shifted = (angle + Math.PI/3) % (2*Math.PI);
//        int sector = (int)(shifted / (2*Math.PI/3)); // 每个扇区 120° = 2PI/3
//        return (sector >= 0 && sector < 3) ? sector : -1;
//    }
//
//    // 鼠标点击事件
//    @Override
//    public boolean mouseClicked(double mouseX, double mouseY, int button) {
//        if (button == 0) { // 左键点击
//            int section = getSectionFromMouse((int)mouseX, (int)mouseY);
//            if (section != -1) {
//                this.selectedValue = section;
//                // 可以在这里执行选择后的逻辑，例如发送消息
//                if (this.minecraft.player != null) {
//                    this.minecraft.player.displayClientMessage(new TextComponent("§a[轮盘] 你选择了: " + SECTIONS[section]), false);
//                }
//                // 关闭界面
//                this.onClose();
//                return true;
//            }
//        }
//        return super.mouseClicked(mouseX, mouseY, button);
//    }
//
//    @Override
//    public void onClose() {
//        // 关闭时清除 selected 临时值
//        super.onClose();
//    }
//
//    @Override
//    public boolean isPauseScreen() {
//        return false; // 游戏不暂停
//    }
//}
