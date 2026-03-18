package cheng.build.rider_syteam.best_match;

import cheng.build.api.IBestMatch;
import cheng.build.init.InitItem;
import cheng.build.init.InitSound;
import cheng.build.item.bottle.bottle.InorganicMatterBottleItem;
import cheng.build.item.bottle.bottle.OrganicMatterBottleItem;
import net.minecraft.sounds.SoundEvent;

public class GorillaDiamond implements IBestMatch {
    @Override
    public String getDisplayName() {
        return "GorillaDiamond";
    }

    @Override
    public OrganicMatterBottleItem OrganicMatter() {
        return InitItem.gorilla.get();
    }

    @Override
    public InorganicMatterBottleItem InorganicMatter() {
        return InitItem.diamond.get();
    }

    @Override
    public SoundEvent BestMatchSound() {
        return InitSound.best_match_gorilla_diamond.get();
    }
}
