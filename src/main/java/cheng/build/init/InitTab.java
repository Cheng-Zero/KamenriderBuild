package cheng.build.init;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InitTab {
    public static CreativeModeTab Kamenrider_Build_Tab;
    public static void load() {
        if (Kamenrider_Build_Tab == null)
            Kamenrider_Build_Tab = new BuildTab();
    }

    private static class BuildTab extends CreativeModeTab{
        public BuildTab() {
            super("kamenrider_build_tab");
        }

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(InitItem.buildDriver.get());
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> pItems) {
            List<Item> l = new ArrayList<>();
            l.add(InitItem.buildDriver.get());
            l.add(InitItem.buildBaseArmor.get());
            l.add(InitItem.buildRabbatArmor.get());
            l.add(InitItem.buildTankArmor.get());
            l.add(InitItem.smash_bottle.get());
            l.add(InitItem.empty_bottle.get());
            l.add(InitItem.rabbat.get());
            l.add(InitItem.tank.get());
            for (Item item : l)
                pItems.add(new ItemStack(item));
        }
    }
}