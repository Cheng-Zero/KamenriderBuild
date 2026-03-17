package cheng.build.item.armor;

import cheng.build.GeoModelPath;
import cheng.build.item.armor.base.ARMOR;
import cheng.build.init.InitSound;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class BuildDriver extends ARMOR {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public static final String trigger = "trigger";
    public static final String organicMatter_item_Name = "organicMatter_item";
    public static final String inorganicMatter_item_Name = "inorganicMatter_item";
    public BuildDriver() {
        super(new easyArmor(0,
                        0,
                        0,
                        InitSound.build_driver_equie,
                        Ingredient.EMPTY,
                        "",
                        1,
                        1),
                EquipmentSlot.LEGS, new Properties());
    }

    @Override
    public GeoModelPath.model getAll() {
        return GeoModelPath.BuildDriver;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "predicate", 0.0F, this::predicate));
    }

    @Override
    public void verifyTagAfterLoad(CompoundTag pCompoundTag) {
        super.verifyTagAfterLoad(pCompoundTag);
        tag(pCompoundTag);
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        super.readShareTag(stack, nbt);
        // 检查并修复Tag
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            tag(tag);
        }
    }

    @Override
    public @Nullable CompoundTag getShareTag(ItemStack stack) {
        return super.getShareTag(stack);
    }

    public CompoundTag tag(CompoundTag nbt){
        if (!nbt.contains("isUse"))
            nbt.putBoolean("isUse", false);
        if (!nbt.contains(organicMatter_item_Name))
            nbt.put(organicMatter_item_Name, savePiece(ItemStack.EMPTY));
        if (!nbt.contains(inorganicMatter_item_Name))
            nbt.put(inorganicMatter_item_Name, savePiece(ItemStack.EMPTY));
        if (!nbt.contains(trigger))
            nbt.putString(trigger, "");
        return nbt;
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        LivingEntity livingEntity = event.getExtraDataOfType(LivingEntity.class).get(0);

        boolean isUse = livingEntity.getItemBySlot(EquipmentSlot.LEGS).getOrCreateTag().getBoolean("isUse");
        // 根据动画标志决定播放哪个动画
        if (isUse) {
            // 播放驱动动画
            event.getController().setAnimation((new AnimationBuilder())
                    .addAnimation("driver", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public static CompoundTag savePiece(ItemStack item) {
        CompoundTag tag = new CompoundTag();
        if (!item.isEmpty()) {
            item.save(tag);
        }
        return tag;
    }

    public static ItemStack loadItem(CompoundTag parent, String key) {
        return parent.contains(key) ? ItemStack.of(parent.getCompound(key)) : ItemStack.EMPTY;
    }
}
