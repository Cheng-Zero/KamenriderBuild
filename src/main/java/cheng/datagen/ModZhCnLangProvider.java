package cheng.datagen;

import cheng.build.init.InitItem;
import cheng.build.init.InitMapping;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModZhCnLangProvider extends BaseUtf8LanguageProvider {
    public ModZhCnLangProvider(DataGenerator gen, String modid, String locale) {
        super(gen, modid, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.kamenrider_build_tab", "假面骑士Build");
        add(InitMapping.ShakeBottle.getCategory(),"假面骑士Build");
        add("key.kamenrider_build.clear_driver.title","你需要清空Build驱动器槽位");
        add("key.kamenrider_build.clear_driver.inorganic_title","你需要清空Build驱动器无机物槽位");
        add("key.kamenrider_build.clear_driver.organic_title","你需要清空Build驱动器有机物槽位");
        add("key.kamenrider_build.clear_driver.air","布什戈么，你还想取出啥！？AIR吗？");
        add("key.kamenrider_build.clear_driver.what_do_you_mean","何意味（笑，我没做适配）");
        add(InitMapping.ShakeBottle.getName(),"摇晃瓶子");
        add(InitItem.buildDriver.get(),"Build驱动器");
        add(InitItem.buildBaseArmor.get(),"Build系统装甲");
        add(InitItem.buildRabbatArmor.get(),"Build 有机物装甲-Rabbat");
        add(InitItem.empty_bottle.get(),"空白满装瓶");
        add(InitItem.rabbat.get(),"满装瓶-兔子");
        add(InitItem.tank.get(),"满装瓶-坦克");
    }
}