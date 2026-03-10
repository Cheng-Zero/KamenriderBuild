package cheng.build.render;

import cheng.build.armor.ARMOR;
import cheng.build.model.ARMORModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public abstract class BASEARMORRenderer<T extends ARMOR> extends GeoArmorRenderer<T> {
    public BASEARMORRenderer(ResourceLocation model , ResourceLocation texture, ResourceLocation animation) {
        super(new ARMORModel<>(model, texture, animation));
        this.headBone = "armorHead";
        this.bodyBone = "armorBody";
        this.rightArmBone = "armorRightArm";
        this.leftArmBone = "armorLeftArm";
        this.rightLegBone = "armorRightLeg";
        this.leftLegBone = "armorLeftLeg";
        this.rightBootBone = "armorRightBoot";
        this.leftBootBone = "armorLeftBoot";
    }
}
