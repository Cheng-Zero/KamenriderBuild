package cheng.build.render;

import cheng.build.bottle.Bottle;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class BottleRenderer<T extends Bottle> extends GeoItemRenderer<T> {
    public BottleRenderer(AnimatedGeoModel<T> modelProvider) {
        super(modelProvider);
    }

    public BottleRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet, AnimatedGeoModel<T> modelProvider) {
        super(dispatcher, modelSet, modelProvider);
    }
}
