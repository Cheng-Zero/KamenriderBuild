package cheng.build.rider_syteam;

import cheng.build.Build;
import cheng.build.SoundUtil;
import cheng.build.api.IBestMatch;
import cheng.build.init.InitItem;
import cheng.build.init.InitSound;
import cheng.build.item.armor.BuildDriver;
import cheng.build.item.bottle.bottle.InorganicMatterBottleItem;
import cheng.build.item.bottle.bottle.OrganicMatterBottleItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = Build.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BestMatchRegistry {
    // 保证线程安全
    private static final Map<OrganicMatterBottleItem, IBestMatch> BestMatch_OrganicMatterBottleItem = new ConcurrentHashMap<>();
    private static final Map<InorganicMatterBottleItem, IBestMatch> BestMatch_InorganicMatterBottleItem = new ConcurrentHashMap<>();
    private static final Map<SoundEvent, IBestMatch> BestMatch = new ConcurrentHashMap<>();
    private static final List<IBestMatch> BEST_MATCHS = new ArrayList<>();
    private static boolean loaded = false;
    private static boolean frozen = false;  // 新增：冻结标志

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            loadProviders();
            frozen = true;  // 加载完成后冻结
            Build.LOGGER.info("BestMatchRegistry 已冻结，共 {} 个最佳搭配", BEST_MATCHS.size());
        });
    }

    private static synchronized void loadProviders() {
        if (loaded) return;

        Build.LOGGER.info("开始加载 IBestMatch 服务提供者...");

        ServiceLoader<IBestMatch> loader = ServiceLoader.load(IBestMatch.class);

        for (IBestMatch provider : loader) {
            registerProvider(provider);
        }

        Build.LOGGER.info("IBestMatch 加载完成，共 {} 个", loader.stream().count());
        loaded = true;
    }

    private static void registerProvider(IBestMatch provider) {
        SoundEvent soundEvent = provider.BestMatchSound();
        var inorganicMatterBottleItem = provider.InorganicMatter();
        var organicMatterBottleItem = provider.OrganicMatter();
        if (soundEvent != null && inorganicMatterBottleItem != null && organicMatterBottleItem != null) {
            if (BestMatch.containsKey(soundEvent)) {
                Build.LOGGER.warn("[Build Bottle] 重复注册最佳搭配: {}", provider.getDisplayName());
                return;
            }

            BestMatch_InorganicMatterBottleItem.put(inorganicMatterBottleItem, provider);
            BestMatch_OrganicMatterBottleItem.put(organicMatterBottleItem, provider);
            BestMatch.put(soundEvent, provider);
            BEST_MATCHS.add(provider);
            Build.LOGGER.debug("已加载: {} -> {}{} -> {}",
                    provider.getClass().getSimpleName(),
                    provider.OrganicMatter().getClass().getSimpleName(),
                    provider.InorganicMatter().getClass().getSimpleName(),
                    soundEvent.getRegistryName());
        } else {
            Build.LOGGER.warn("Provider {} 返回的 FullBottle 为 null",
                    provider.getClass().getSimpleName());
        }
    }

    // 手动注册方法（用于非 ServiceLoader 的情况）
    public static synchronized IBestMatch register(IBestMatch provider) {
        if (frozen) {
            throw new IllegalStateException("不能在注册表冻结后注册新的瓶子");
        }
        registerProvider(provider);
        return provider;
    }

    // 便捷的匿名类注册方法
    public static synchronized IBestMatch register(
            String Name,
            OrganicMatterBottleItem OrganicMatterBottleItem,
            InorganicMatterBottleItem InorganicMatterBottleItem,
            SoundEvent soundEvent) {

        if (frozen) {
            throw new IllegalStateException("不能在注册表冻结后注册新的瓶子");
        }

        IBestMatch provider = new IBestMatch() {
            @Override
            public String getDisplayName() {
                return Name;
            }

            @Override
            public OrganicMatterBottleItem OrganicMatter() {
                return OrganicMatterBottleItem;
            }

            @Override
            public InorganicMatterBottleItem InorganicMatter() {
                return InorganicMatterBottleItem;
            }

            @Override
            public SoundEvent BestMatchSound() {
                return soundEvent;
            }
        };

        registerProvider(provider);
        return provider;
    }

    // 确保在使用前已加载
    private static void ensureLoaded() {
        if (!loaded) {
            loadProviders();
        }
    }

    public static IBestMatch findByBottle(Item item) {
        ensureLoaded();
        if (item instanceof InorganicMatterBottleItem)
            return BestMatch_InorganicMatterBottleItem.get(item);
        else if (item instanceof OrganicMatterBottleItem)
            return BestMatch_OrganicMatterBottleItem.get(item);
        return null;
    }

    public static IBestMatch findByItemStack(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return null;
        ensureLoaded();
        if (stack.getItem() instanceof InorganicMatterBottleItem){
            return BestMatch_InorganicMatterBottleItem.get(stack.getItem());
        }else if (stack.getItem() instanceof OrganicMatterBottleItem)
            return BestMatch_OrganicMatterBottleItem.get(stack.getItem());
        return null;
    }

    public static List<IBestMatch> getAllBestMatchs() {
        ensureLoaded();
        return Collections.unmodifiableList(BEST_MATCHS);
    }

    public static boolean isLoaded() {
        return loaded;
    }

    public static boolean isFrozen() {
        return frozen;
    }

    public static boolean isBestMatch(Player player) {
        ItemStack driver = player.getItemBySlot(EquipmentSlot.LEGS);

        boolean false_1 = driver.isEmpty(),
                false_2 = driver.getItem() != InitItem.buildDriver.get();

        if (false_1 || false_2) return false;

        CompoundTag tag = Objects.requireNonNull(driver.getTag());

        ItemStack
                organicMatter_item = BuildDriver.loadItem(tag, BuildDriver.organicMatter_item_Name),
                inorganicMatter_item = BuildDriver.loadItem(tag, BuildDriver.inorganicMatter_item_Name);
        Item
                organic = organicMatter_item.getItem(),
                inorganic = inorganicMatter_item.getItem();
        return getAllBestMatchs().stream().anyMatch(d-> organic.equals(d.OrganicMatter()) && inorganic.equals((d.InorganicMatter())));
    }

    public static void playSound(Player player,ItemStack inorganic,ItemStack organic) {
        getAllBestMatchs().stream()
                .filter(bo -> bo.InorganicMatter() == inorganic.getItem() && bo.OrganicMatter() == organic.getItem())
                .findFirst()
                .ifPresent((D)-> SoundUtil.playSound(player.level,(Entity) player,D.BestMatchSound(), SoundSource.PLAYERS));
    }

    public static void playBestMatchSound(Player player) {
        SoundUtil.playSound(player.level,(Entity) player, InitSound.best_match.get(), SoundSource.PLAYERS);
    }
    public static int size() {
        return BEST_MATCHS.size();
    }

    // 清空方法（仅在测试或特殊情况使用）
    public static synchronized void clear() {
        if (frozen) {
            Build.LOGGER.warn("尝试清空已冻结的注册表，这可能会导致问题");
        }
        BestMatch_InorganicMatterBottleItem.clear();
        BestMatch_OrganicMatterBottleItem.clear();
        BestMatch.clear();
        BEST_MATCHS.clear();
        loaded = false;
        frozen = false;
    }

    enum BestMatchType{
        BEST_MATCH(InitSound.best_match.get()),
        SUPER_BEST_MATCH(InitSound.super_best_match.get());
        private final SoundEvent soundEvent;

        BestMatchType(SoundEvent soundEvent) {
            this.soundEvent = soundEvent;
        }

        public SoundEvent getSoundEvent() {
            return soundEvent;
        }
    }
}
