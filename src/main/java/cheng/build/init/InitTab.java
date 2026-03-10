package cheng.build.init;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

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
            pItems.add(add(InitItem.buildDriver.get()));
            pItems.add(add(InitItem.buildBaseArmor.get()));
            pItems.add(add(InitItem.buildRabbatArmor.get()));
            pItems.add(add(InitItem.buildTankArmor.get()));
            pItems.add(add(InitItem.empty_bottle.get()));
            pItems.add(add(InitItem.rabbat.get()));
            pItems.add(add(InitItem.tank.get()));
        }
        ItemStack add(Item item){
            return new ItemStack(item);
        }
    }
}