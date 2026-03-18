package cheng.build.api;

import cheng.build.item.bottle.bottle.InorganicMatterBottleItem;
import cheng.build.item.bottle.bottle.OrganicMatterBottleItem;
import net.minecraft.sounds.SoundEvent;

public interface IBestMatch {
    String getDisplayName();
    OrganicMatterBottleItem OrganicMatter();
    InorganicMatterBottleItem InorganicMatter();
    SoundEvent BestMatchSound();
}
