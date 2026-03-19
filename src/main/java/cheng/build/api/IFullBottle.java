package cheng.build.api;

import cheng.build.item.armor.base.BuildArmor;
import cheng.build.item.bottle.bottle.FullBottle;
import cheng.build.item.bottle.bottle_effect.BottleMobEffect;
import net.minecraft.sounds.SoundEvent;

public interface IFullBottle {
    String getName();

    FullBottle getFullBottle();

    /// 药水效果
    BottleMobEffect BottleMobEffect();

    /// 音效
    SoundEvent sound();

    /// 返回 满装瓶对应的Build装甲
    BuildArmor getBuildArmor();
}
