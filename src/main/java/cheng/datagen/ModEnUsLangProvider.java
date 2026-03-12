package cheng.datagen;

import cheng.build.init.InitItem;
import cheng.build.init.InitMapping;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModEnUsLangProvider extends LanguageProvider {
    public ModEnUsLangProvider(DataGenerator gen, String modid, String locale) {
        super(gen, modid, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.kamenrider_build_tab", "KamenRider Build");
        add("key.categories.kamenrider_build","KamenRider Build");
        add("key.kamenrider_build.clear_driver.title","You need Clear Build Driver Slot");
        add("key.kamenrider_build.clear_driver.inorganic_title","You need Clear Build Driver InorganicMatter Slot");
        add("key.kamenrider_build.clear_driver.organic_title","You need Clear Build Driver OrganicMatter Slot");
        add("key.kamenrider_build.clear_driver.air","Not Bro, what else do you want to take out, AIR?");
        add("key.kamenrider_build.clear_driver.what_do_you_mean","What do you mean?");
        add(InitMapping.ShakeBottle.getName(),"Shake Bottle");
        add(InitItem.buildDriver.get(),"Build Driver");
        add(InitItem.buildBaseArmor.get(),"Base Build Armor");
        add(InitItem.buildRabbatArmor.get(),"Build-Rabbat-Armor");
        add(InitItem.buildTankArmor.get(),"Build-Tank-Armor");
        add(InitItem.smash_bottle.get(),"Smash Bottle");
        add(InitItem.empty_bottle.get(),"Empty Bottle");
        add(InitItem.rabbat.get(),"Rabbat FullBottle");
        add(InitItem.tank.get(),"Tank FullBottle");
        add(InitItem.fullbottle_purifier.get(),"FullBottlePurifier");
        add("block.kamenrider_build.fullbottle_purifier.start","Purification started");
        add("block.kamenrider_build.fullbottle_purifier.stop","Purification stopped");
    }
}