package cheng.build.block.entity;

import cheng.build.Build;
import cheng.build.Config;
import cheng.build.ItemHelper;
import cheng.build.item.bottle.bottles.SmashBottleItem;
import cheng.build.init.InitModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class FullbottlePurifierBlockEntity extends BlockEntity implements IAnimatable, BlockEntityTicker<FullbottlePurifierBlockEntity> {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
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
    private boolean Processing = false;
    private int Progress = 0;
    private String animation = "close";
    public FullbottlePurifierBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(InitModBlockEntities.fullbottle_purifler.get(), pPos, pBlockState);
    }
    // 写入物品存储NBT
    private CompoundTag wirteItems(CompoundTag tag){
        super.saveAdditional(tag);
        tag.put("Inventory",this.inventory.serializeNBT());
        tag.putBoolean("Processing",this.Processing);
        tag.putInt("Progress",this.Progress);
        tag.putString("animation",this.animation);
        return tag;
    }
    // 载入物品存储NBT
    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("Inventory")) this.inventory.deserializeNBT(pTag.getCompound("Inventory"));
        else this.inventory.deserializeNBT(pTag);

        if (pTag.contains("Processing")) this.Processing = pTag.getBoolean("Processing");

        if (pTag.contains("Progress")) this.Progress = pTag.getInt("Progress");

        if (pTag.contains("animation")) this.animation = pTag.getString("animation");
    }

    // 发送物品存储NBT
    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        if (tag.contains("Inventory")) this.inventory.deserializeNBT(tag.getCompound("Inventory"));

        if (tag.contains("Processing")) this.Processing = tag.getBoolean("Processing");

        if (tag.contains("Progress")) this.Progress = tag.getInt("Progress");

        if (tag.contains("animation")) this.animation = tag.getString("animation");

    }
    @Override
    public void tick(Level pLevel, BlockPos pPos, BlockState pState, FullbottlePurifierBlockEntity pBlockEntity) {
        if (pLevel.isClientSide() || this.level == null)return;
        ItemStack stack = this.inventory.getStackInSlot(0);
        if (this.Processing && !stack.isEmpty() && stack.getItem() instanceof SmashBottleItem){
            if (this.Progress % 250 == 0)
                Build.LOGGER.info("净化中{},达{}净化完成",this.Progress,Config.fullbottlepurifierprogress);
            this.Progress++;
            if (this.Progress >=Config.fullbottlepurifierprogress) {
                this.inventory.setStackInSlot(0, getRandomConfigItem().split(1));
                // 重置进度
                this.Progress = 0;
                // 停止净化
                this.Processing = false;
                setAnimation("open");
                setChanged();
                this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
                Build.LOGGER.info("净化完成！");
            }
        }
    }
    public void add(ItemStack itemStack, Player player) {
        if (this.level != null) {
            // 获取当前槽位的物品
            ItemStack currentStack = this.inventory.getStackInSlot(0);

            switch (this.animation){
                case "open":{
                    if (player.isCrouching()) {
                        setAnimation("close");
                        if (!currentStack.isEmpty() && currentStack.getItem() instanceof SmashBottleItem) {
                            this.Processing = true;
                            this.Progress = 0;
                            player.displayClientMessage(new TranslatableComponent("block.kamenrider_build.fullbottle_purifier.start"),true);
                            Build.LOGGER.info("玩家:{}UUID:{}启动坐标{}的满瓶净化器",player.getName().getString(),player.getUUID(),this.worldPosition);
                        }
                    }
                    else {
                        if (currentStack.isEmpty() && itemStack.getItem() instanceof SmashBottleItem) {
                            this.inventory.setStackInSlot(0, itemStack.split(1));
                        } else {
                            ItemHelper.giveItem(player, currentStack.split(1));
                            this.inventory.setStackInSlot(0, ItemStack.EMPTY);
                        }
                    }
                    break;
                }
                case "close":{
                    if (player.isCrouching()){
                        setAnimation("open");
                        this.Processing = false;
                        this.Progress = 0;
                        if (currentStack.getItem() instanceof SmashBottleItem) {
                            player.displayClientMessage(new TranslatableComponent("block.kamenrider_build.fullbottle_purifier.stop"), true);
                            Build.LOGGER.info("玩家{}UUID:{}停止坐标{}的满瓶净化器", player.getName().getString(), player.getUUID(), this.worldPosition);
                        }
                    }
                    break;
                }
            }

            setChanged();
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }
    public ItemStackHandler getInventory(){
        return this.inventory;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this,"idle",5,this::idle));
    }
    private <E extends IAnimatable> PlayState idle(AnimationEvent<E> event){
        if (this.animation.equals("open")) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("open"));
        }else
        if (this.animation.equals("close")) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("close"));
        }

       return PlayState.CONTINUE;
    }

    public void setAnimation(String animName) {
        if (!Objects.equals(this.animation, animName)) {
            this.animation = animName;
            // 触发同步到客户端
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    }
    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    // 上传物品存储NBT
    @Override
    public CompoundTag getUpdateTag() {
        return this.wirteItems(new CompoundTag());
    }
    // 保存物品存储NBT
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        this.wirteItems(pTag);
    }

    private static final Random RANDOM = new Random();

    /**
     * 从配置中随机选择一个物品
     */
    private ItemStack getRandomConfigItem() {
        if (Config.items == null || Config.items.isEmpty()) {
            return ItemStack.EMPTY;
        }

        List<Item> itemList = new ArrayList<>(Config.items);
        Item randomItem = itemList.get(RANDOM.nextInt(itemList.size()));

        return new ItemStack(randomItem, 1); // 数量为1
    }
}
