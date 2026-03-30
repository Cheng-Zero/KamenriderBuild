package cheng.build.item.armor.base;

import cheng.build.GeoModelPath;
import cheng.build.program.RotaryDriverKeyProgram.IHenshin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public abstract class BaseDriver extends ARMOR {
    public BaseDriver(ArmorMaterial materialIn, EquipmentSlot slot, Properties builder) {
        super(materialIn, slot, builder);
    }

    public GeoModelPath.model getAll() {
        return GeoModelPath.BuildDriver;
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
