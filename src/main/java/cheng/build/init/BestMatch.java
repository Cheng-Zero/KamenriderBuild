package cheng.build.init;

import cheng.build.Build;
import cheng.build.SoundUtil;
import cheng.build.armor.BuildDriver;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.*;
import java.util.function.Supplier;

public record BestMatch(Item organicMatter, Item inorganicMatter, Supplier<SoundEvent> sound) {
    private static final Map<Item,BestMatch> BestMatch_Organic = new HashMap<>();
    private static final Map<Item,BestMatch> BestMatch_Inorganic = new HashMap<>();
    private static final Map<Supplier<SoundEvent>,BestMatch> BestMatch_Sound = new HashMap<>();
    private static final List<BestMatch> bestMatch = new ArrayList<>();
    static {
        registryBestMatch(Items.AIR,Items.AIR, InitSound.EMPTY);
        registryBestMatch(InitItem.rabbat.get(), InitItem.tank.get(), InitSound.best_match);
    }

    public static void registryBestMatch(Item organicMatter, Item inorganicMatter,Supplier<SoundEvent> sound){
        if (BestMatch_Organic.containsKey(organicMatter) || BestMatch_Inorganic.containsKey(inorganicMatter)) {
            // 可以添加日志警告
            Build.LOGGER.debug("出现重复注册");
            return;
        }
        BestMatch b = new BestMatch(organicMatter, inorganicMatter, sound);
        BestMatch_Organic.put(organicMatter,b);
        BestMatch_Inorganic.put(inorganicMatter,b);
        BestMatch_Sound.put(sound,b);
        bestMatch.add(b);
    }

    public static List<BestMatch> getAllBestMatch(){
        return bestMatch;
    }

    public static boolean isBestMatch(Player player) {
        ItemStack driver = player.getItemBySlot(EquipmentSlot.LEGS);

        boolean false_1 = driver.isEmpty(),
                false_2 = driver.getItem() != InitItem.buildDriver.get();

        if (false_1 || false_2) return false;

        CompoundTag tag = Objects.requireNonNull(driver.getTag());

        ItemStack
                organicMatter_item = BuildDriver.loadItem(tag, "organicMatter_item"),
                inorganicMatter_item = BuildDriver.loadItem(tag, "inorganicMatter_item");
        Item
                organic = organicMatter_item.getItem(),
                inorganic = inorganicMatter_item.getItem();

        return organic.equals((getCurrentMatch(driver).organicMatter)) &&
                inorganic.equals((getCurrentMatch(driver).inorganicMatter));
    }

    // 根据物品判断当前是什么BestMatch
    public static BestMatch getCurrentMatch(ItemStack legItem) {
        if (legItem.isEmpty() || legItem.getTag() == null) return bestMatch.get(0);

        CompoundTag tag = legItem.getTag();

        ItemStack
                organicMatter_item = BuildDriver.loadItem(tag, BuildDriver.organicMatter_item_Name),
                inorganicMatter_item = BuildDriver.loadItem(tag, BuildDriver.inorganicMatter_item_Name);
        Item
                organic = organicMatter_item.getItem(),
                inorganic = inorganicMatter_item.getItem();

        for (BestMatch batch : getAllBestMatch()) {
            if (batch == bestMatch.get(0))
                continue;
            if (batch.organicMatter.equals(organic) && batch.inorganicMatter.equals(inorganic)) {
                return batch;
            }
        }
        return bestMatch.get(0);
    }

    public static void playSound(Player player) {
        ItemStack itemBySlot = player.getItemBySlot(EquipmentSlot.LEGS);
        SoundUtil.playSound(player.level, (ServerPlayer) player, getCurrentMatch(itemBySlot).sound.get(), SoundSource.PLAYERS);
    }
}
