package cheng.build.block;

import cheng.build.GeoModelPath;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import net.minecraft.resources.ResourceLocation;


public class FullbottlePurifierBlockModel<T extends IAnimatable> extends AnimatedGeoModel<T> {
	@Override
	public ResourceLocation getModelLocation(T object) {
		return GeoModelPath.fullbottle_purifier.model();
	}

	@Override
	public ResourceLocation getTextureLocation(T object) {
		return GeoModelPath.fullbottle_purifier.texture();
	}

	@Override
	public ResourceLocation getAnimationFileLocation(T animatable) {
		return GeoModelPath.fullbottle_purifier.animation();
	}
}
