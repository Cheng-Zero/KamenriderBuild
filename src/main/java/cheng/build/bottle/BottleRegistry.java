package cheng.build.bottle;

import cheng.build.Build;
import cheng.build.api.IFullBottle;
import cheng.build.item.armor.base.BuildArmor;
import cheng.build.item.bottle.bottle.FullBottle;
import cheng.build.item.bottle.bottle_effect.BottleMobEffect;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.*;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = Build.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BottleRegistry {
    // 使用 ConcurrentHashMap 保证线程安全
    private static final Map<Item, IFullBottle> BOTTLE_MAP = new ConcurrentHashMap<>();
    private static final List<IFullBottle> BOTTLES = new ArrayList<>();
    private static boolean loaded = false;
    private static boolean frozen = false;  // 新增：冻结标志

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            loadProviders();
            frozen = true;  // 加载完成后冻结
            Build.LOGGER.info("BottleRegistry 已冻结，共 {} 个瓶子", BOTTLES.size());
        });
    }

    private static synchronized void loadProviders() {
        if (loaded) return;

        Build.LOGGER.info("开始加载 IFullBottle 服务提供者...");

        ServiceLoader<IFullBottle> loader = ServiceLoader.load(IFullBottle.class);
        int count = 0;

        for (IFullBottle provider : loader) {
            registerProvider(provider);
            count++;
        }

        Build.LOGGER.info("IFullBottle 加载完成，共 {} 个", count);
        loaded = true;
    }

    private static void registerProvider(IFullBottle provider) {
        Item item = provider.getFullBottle();
        if (item != null) {
            if (BOTTLE_MAP.containsKey(provider.getFullBottle())) {
                Build.LOGGER.warn("[Build Bottle] 重复注册满装瓶: {}", provider.getName());
                return;
            }
            if (BOTTLE_MAP.containsKey(provider.getBuildArmor())) {
                Build.LOGGER.warn("[Build Bottle] 重复注册满装瓶装甲: {}", provider.getBuildArmor());
                return;
            }

            BOTTLE_MAP.put(item, provider);
            BOTTLES.add(provider);
            Build.LOGGER.debug("已加载: {} -> {}",
                    provider.getClass().getSimpleName(),
                    item.getRegistryName());
        } else {
            Build.LOGGER.warn("Provider {} 返回的 FullBottle 为 null",
                    provider.getClass().getSimpleName());
        }
    }

    // 手动注册方法（用于非 ServiceLoader 的情况）
    public static synchronized IFullBottle register(IFullBottle provider) {
        if (frozen) {
            throw new IllegalStateException("不能在注册表冻结后注册新的瓶子");
        }
        registerProvider(provider);
        return provider;
    }

    // 便捷的匿名类注册方法
    public static synchronized IFullBottle register(
            FullBottle fullBottle,
            BottleMobEffect effect,
            SoundEvent sound,
            BuildArmor armor,
            String name) {

        if (frozen) {
            throw new IllegalStateException("不能在注册表冻结后注册新的瓶子");
        }

        IFullBottle provider = new IFullBottle() {
            @Override
            public FullBottle getFullBottle() {
                return fullBottle;
            }

            @Override
            public BottleMobEffect BottleMobEffect() {
                return effect;
            }

            @Override
            public SoundEvent sound() {
                return sound;
            }

            @Override
            public BuildArmor getBuildArmor() {
                return armor;
            }

            @Override
            public String getName() {
                return name;
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

    public static IFullBottle findByItem(Item item) {
        ensureLoaded();
        return BOTTLE_MAP.get(item);
    }

    public static IFullBottle findByItemStack(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return null;
        ensureLoaded();
        return BOTTLE_MAP.get(stack.getItem());
    }

    public static List<IFullBottle> getAllBottles() {
        ensureLoaded();
        return Collections.unmodifiableList(BOTTLES);
    }

    public static boolean isLoaded() {
        return loaded;
    }

    public static boolean isFrozen() {
        return frozen;
    }

    public static int size() {
        return BOTTLES.size();
    }

    // 清空方法（仅在测试或特殊情况使用）
    public static synchronized void clear() {
        if (frozen) {
            Build.LOGGER.warn("尝试清空已冻结的注册表，这可能会导致问题");
        }
        BOTTLES.clear();
        BOTTLE_MAP.clear();
        loaded = false;
        frozen = false;
    }
}
