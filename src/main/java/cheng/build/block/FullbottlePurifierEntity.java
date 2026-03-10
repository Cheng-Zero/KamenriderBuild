package cheng.build.block;

import cheng.build.ItemHelper;
import cheng.build.bottle.Bottle;
import cheng.build.init.InitModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class FullbottlePurifierEntity extends BlockEntity implements IAnimatable {
    private ItemStackHandler inventory = new ItemStackHandler(1){
        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
            return 1;
        }
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            // 当物品变化时，通知客户端更新
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    };
    public FullbottlePurifierEntity(BlockPos pPos, BlockState pBlockState) {
        super(InitModBlockEntities.fullbottle_purifler.get(), pPos, pBlockState);
    }
    // 写入物品存储NBT
    private CompoundTag wirteItems(CompoundTag tag){
        super.saveAdditional(tag);
        tag.put("Inventory",this.inventory.serializeNBT());
        return tag;
    }

    // 载入物品存储NBT
    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("Inventory")){
            this.inventory.deserializeNBT(pTag.getCompound("Inventory"));
        }else {
            this.inventory.deserializeNBT(pTag);
        }
    }
    // 上传物品存储NBT
    @Override
    public CompoundTag getUpdateTag() {
        return this.wirteItems(new CompoundTag());
    }
    // 发送物品存储NBT
    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        if (tag.contains("Inventory")) {
            this.inventory.deserializeNBT(tag.getCompound("Inventory"));
        }
    }
    // 保存物品存储NBT
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        this.wirteItems(pTag);
    }

    public void add(ItemStack itemStack, Player player) {
        if (this.level != null)
            if (this.inventory.getStackInSlot(0).isEmpty()) {
                this.inventory.setStackInSlot(0, itemStack.split(1));
                setChanged();
                this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            } else {
                ItemHelper.giveItem(player,this.inventory.getStackInSlot(0).split(1));
                this.inventory.setStackInSlot(0, ItemStack.EMPTY);
                setChanged();
                this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            }
    }
    public ItemStackHandler getInventory(){
        return this.inventory;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this,"idle",0,this::d));
    }
    <P extends IAnimatable> PlayState d(AnimationEvent<P> a){
        a.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return GeckoLibUtil.createFactory(this);
    }
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
