package cheng.build.datagen;

import cheng.build.init.InitItem;
import cheng.build.init.InitMapping;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import org.apache.commons.lang3.text.translate.JavaUnicodeEscaper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ModLangProvider {
    record translation(String key, String en_us,String zh_ch){}
    static List<translation> t = new ArrayList<>();
    static {
        add("itemGroup.kamenrider_build_tab", "KamenRider Build", "假面骑士Build");
        add("key.categories.kamenrider_build","KamenRider Build","假面骑士Build");
        add("key.kamenrider_build.clear_driver.title","You need Clear Build Driver Slot","你需要清空Build驱动器槽位");
        add("key.kamenrider_build.clear_driver.inorganic_title","You need Clear Build Driver InorganicMatter Slot","你需要清空Build驱动器无机物槽位");
        add("key.kamenrider_build.clear_driver.organic_title","You need Clear Build Driver OrganicMatter Slot","你需要清空Build驱动器有机物槽位");
        add("key.kamenrider_build.clear_driver.air","Not Bro, what else do you want to take out, AIR?","布什戈么，你还想取出啥！？AIR吗？");
        add("key.kamenrider_build.clear_driver.what_do_you_mean","What do you mean?","何意味");
        add(InitMapping.ShakeBottle.getName(),"Shake Bottle","摇晃满装瓶");
        add(InitMapping.ClearDriver.getName(),"Clear Driver","清空驱动器槽位");
        add(InitMapping.RotaryDriver.getName(),"Rotary Driver","摇动驱动器");
        add(InitItem.buildDriver.get(),"Build Driver","Build驱动器");
        add(InitItem.buildBaseArmor.get(),"Build Syteam Armor","Build系统装甲");
        add(InitItem.buildRabbatArmor.get(),"Build-Rabbat-Armor","Build 有机物装甲-Rabbat");
        add(InitItem.buildGorillaArmor.get(),"Build-Gorilla-Armor","Build 有机物装甲-Gorilla");
        add(InitItem.buildTankArmor.get(),"Build-Tank-Armor","Build 无机物装甲-Tank");
        add(InitItem.buildDiamondArmor.get(),"Build-Diamond-Armor","Build 无机物装甲-Diamond");
        add(InitItem.smash_bottle.get(),"Smash Bottle","猛击者满装瓶");
        add(InitItem.empty_bottle.get(),"Empty Bottle","空白满装瓶");
        add(InitItem.rabbat.get(),"Rabbat FullBottle","满装瓶-兔子");
        add(InitItem.gorilla.get(),"Gorilla FullBottle","满装瓶-猩猩");
        add(InitItem.tank.get(),"Tank FullBottle","满装瓶-坦克");
        add(InitItem.diamond.get(),"Diamond FullBottle","满装瓶-钻石");
        add(InitItem.fullbottle_purifier.get(),"FullBottlePurifier","满装瓶净化机");
        add("block.kamenrider_build.fullbottle_purifier.start","Purification started","净化开始");
        add("block.kamenrider_build.fullbottle_purifier.stop","Purification stopped","净化停止");

        add("commands.kamenrider_build.hazard_level.success.self","Set own hazard level to %s","已将自己的危险等级设置为%s");
        add("commands.kamenrider_build.hazard_level.success.other","Set %s's hazard level to %s","已将%s的危险等级设置为%s");
        add("commands.kamenrider_build.hazard_level.changed","Your hazard level has been updated to %s","你的危险等级已更新为%s");
        add("commands.kamenrider_build.hazard_level.get","%s's hazard level to %s","%s的危险等级为%s");
    }
    protected static void add(String key,String en_us,String zh_cn){
        t.add(new translation(key, en_us, zh_cn));
    }
    protected static void add(Item key,String en_us,String zh_cn){
        t.add(new translation(key.getDescriptionId(), en_us, zh_cn));
    }
    public static class ModEnUsLangProvider extends LanguageProvider {
        public ModEnUsLangProvider(DataGenerator gen, String modid) {
            super(gen, modid, "en_us");
        }
        protected void addTranslations() {
            t.forEach(d -> add(d.key,d.en_us));
        }
    }
    public static class ModZhCnLangProvider extends BaseUtf8LanguageProvider {
        public ModZhCnLangProvider(DataGenerator gen, String modid) {
            super(gen, modid,  "zh_cn");
        }
        protected void addTranslations() {
            t.forEach(d -> add(d.key,d.zh_ch));
        }
    }
}