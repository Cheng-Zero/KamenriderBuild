package cheng.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

public abstract class BaseUtf8LanguageProvider implements DataProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private final Map<String, String> data = new TreeMap<>();
    private final DataGenerator generator;
    private final String modid;
    private final String locale;

    public BaseUtf8LanguageProvider(DataGenerator gen, String modid, String locale) {
        this.generator = gen;
        this.modid = modid;
        this.locale = locale;
        addTranslations();
    }

    protected abstract void addTranslations();

    public void add(String key, String value) {
        data.put(key, value);
    }
    public void add(Item key, String name) {
        add(key.getDescriptionId(), name);
    }
    public void addBlock(Supplier<? extends Block> key, String name) {
        add(key.get(), name);
    }

    public void add(Block key, String name) {
        add(key.getDescriptionId(), name);
    }

    public void addItem(Supplier<? extends Item> key, String name) {
        add(key.get(), name);
    }

    public void addItemStack(Supplier<ItemStack> key, String name) {
        add(key.get(), name);
    }

    public void add(ItemStack key, String name) {
        add(key.getDescriptionId(), name);
    }

    public void addEnchantment(Supplier<? extends Enchantment> key, String name) {
        add(key.get(), name);
    }

    public void add(Enchantment key, String name) {
        add(key.getDescriptionId(), name);
    }
    public void addEffect(Supplier<? extends MobEffect> key, String name) {
        add(key.get(), name);
    }

    public void add(MobEffect key, String name) {
        add(key.getDescriptionId(), name);
    }

    public void addEntityType(Supplier<? extends EntityType<?>> key, String name) {
        add(key.get(), name);
    }

    public void add(EntityType<?> key, String name) {
        add(key.getDescriptionId(), name);
    }

    @Override
    public void run(HashCache cache) throws IOException {
        Path target = this.generator.getOutputFolder()
            .resolve("assets/" + modid + "/lang/" + locale + ".json");
        
        // 确保目标目录存在
        Files.createDirectories(target.getParent());
        
        JsonElement json = GSON.toJsonTree(data);
        
        // 使用 UTF-8 编码写入文件
        try (BufferedWriter writer = Files.newBufferedWriter(target, 
                java.nio.charset.StandardCharsets.UTF_8)) {
            GSON.toJson(json, writer);
        }
    }

    @Override
    public String getName() {
        return "UTF-8 Languages: " + locale;
    }
}