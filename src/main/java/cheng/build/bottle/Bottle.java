package cheng.build.bottle;

import cheng.build.render.BottleRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.function.Consumer;

public abstract class Bottle extends Item implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public Bottle(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(bottle_renderer());
    }
    public abstract BottleRenderer renderer();

    private IItemRenderProperties bottle_renderer(){
        return new IItemRenderProperties() {
            private final BlockEntityWithoutLevelRenderer renderer = renderer();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        };
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        InteractionResultHolder<ItemStack> ar = super.use(pLevel, pPlayer, pUsedHand);
        // 获取 调用 方法体
        BottleExecute bottleExecute = new BottleExecute();
        // 更新数据
        bottleExecute.update(pPlayer);

        bottleExecute.OnDriver(ar.getObject());
        return ar;
    }

    @Override
    public void registerControllers(AnimationData data) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
