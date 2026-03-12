package cheng.build.entity;

import cheng.build.GeoModelPath;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class OrganicEntity extends EffectEntity{
    public OrganicEntity(Level pLevel, boolean isBestMatch, ResourceLocation texrure) {
        super(pLevel);
        this.setModel(GeoModelPath.BuildRabbatArmor.model());
        this.setTexture(texrure);
        this.setAnimations(new ResourceLocation("kamenrider_build:animations/build_henshin.animation.json"));

        if (isBestMatch){
            this.setAnimation("best_match");
        }
        else {
            if (pLevel.getRandom().nextFloat() >= 0.5)
                this.setAnimation("on_organic");
            else
                this.setAnimation("on_organic2");
        }
    }
}
