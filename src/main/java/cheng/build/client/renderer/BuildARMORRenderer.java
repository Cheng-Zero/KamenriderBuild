package cheng.build.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class BuildARMORRenderer extends BASEARMORRenderer{
    public BuildARMORRenderer(ResourceLocation model, ResourceLocation texture, ResourceLocation animation) {
        super(model, texture, animation);
    }

    @Override
    public GeoArmorRenderer applySlot(EquipmentSlot slot) {
        super.applySlot(slot);
        this.setBoneVisibility(this.headBone, true);
        this.setBoneVisibility(this.bodyBone, true);
        this.setBoneVisibility(this.leftArmBone, true);
        this.setBoneVisibility(this.leftLegBone, true);
        this.setBoneVisibility(this.rightArmBone, true);
        this.setBoneVisibility(this.rightLegBone, true);
        this.setBoneVisibility(this.leftBootBone, true);
        this.setBoneVisibility(this.rightBootBone, true);
        return this;
    }
}
