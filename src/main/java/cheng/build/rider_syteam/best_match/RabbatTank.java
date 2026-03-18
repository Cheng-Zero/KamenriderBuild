package cheng.build.rider_syteam.best_match;

import cheng.build.api.IBestMatch;
import cheng.build.init.InitItem;
import cheng.build.init.InitSound;
import cheng.build.item.bottle.bottle.InorganicMatterBottleItem;
import cheng.build.item.bottle.bottle.OrganicMatterBottleItem;
import net.minecraft.sounds.SoundEvent;

public class RabbatTank implements IBestMatch {
    @Override
    public String getDisplayName() {
        return "RabbatTank";
    }

    @Override
    public OrganicMatterBottleItem OrganicMatter() {
        return InitItem.rabbat.get();
    }

    @Override
    public InorganicMatterBottleItem InorganicMatter() {
        return InitItem.tank.get();
    }

    @Override
    public SoundEvent BestMatchSound() {
        return InitSound.best_match_rabbat_tank.get();
    }
}
