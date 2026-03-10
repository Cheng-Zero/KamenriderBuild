package cheng.build.init;

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

import java.util.Objects;
import java.util.function.Supplier;

public class BestMatch {
    public enum bestMatch {
        NULL(Items.AIR,Items.AIR, InitSound.EMPTY),
        RABBAT_TANK(InitItem.rabbat.get(), InitItem.tank.get(), InitSound.best_match);
        private final Item organicMatter;
        private final Item inorganicMatter;
        private final Supplier<SoundEvent> sound;

        bestMatch(Item organicMatter, Item inorganicMatter, Supplier<SoundEvent> sound) {
            this.organicMatter = organicMatter;
            this.inorganicMatter = inorganicMatter;
            this.sound = sound;
        }
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
    public static bestMatch getCurrentMatch(ItemStack legItem) {
        if (legItem.isEmpty() || legItem.getTag() == null) return bestMatch.NULL;

        CompoundTag tag = legItem.getTag();

        ItemStack
                organicMatter_item = BuildDriver.loadItem(tag, "organicMatter_item"),
                inorganicMatter_item = BuildDriver.loadItem(tag, "inorganicMatter_item");
        Item
                organic = organicMatter_item.getItem(),
                inorganic = inorganicMatter_item.getItem();

        for (bestMatch batch : bestMatch.values()) {
            if (batch == bestMatch.NULL)
                continue;
            if (batch.organicMatter.equals(organic) && batch.inorganicMatter.equals(inorganic)) {
                return batch;
            }
        }
        return bestMatch.NULL;
    }

    public static void playSound(Player player) {
        SoundUtil.playSound(player.level, (ServerPlayer) player, getCurrentMatch(player.getItemBySlot(EquipmentSlot.LEGS)).sound.get(), SoundSource.PLAYERS);
    }
}
