package cheng.build.api;

import cheng.build.GeoModelPath;
import net.minecraft.resources.ResourceLocation;

public interface IModel {
    /// 获取包括模型纹理动画的类型数据
    GeoModelPath.model getAll();
    /// 获取模型
    default ResourceLocation getModel(){
        return getAll().model();
    }
    /// 获取纹理
    default ResourceLocation getTexture(){
        return getAll().texture();
    }
    /// 获取动画
    default ResourceLocation getAnimations() {
        return getAll().animation();
    }

}
