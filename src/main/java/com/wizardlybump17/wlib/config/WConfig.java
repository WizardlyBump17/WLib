package com.wizardlybump17.wlib.config;

import com.wizardlybump17.wlib.item.ItemBuilder;
import com.wizardlybump17.wlib.util.ListUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Getter
public class WConfig extends YamlConfiguration {

    private final File file;
    private final JavaPlugin plugin;
    private final String name;

    public WConfig(JavaPlugin plugin, String name) {
        file = new File((this.plugin = plugin).getDataFolder(), this.name = name);
    }

    public void reloadConfig() {
        try {
            load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isNull(String path) {
        return get(path) == null;
    }

    @Override
    public void set(String path, Object value) {
        if (value instanceof ConfigurationSerializable) {
            set(path, ((ConfigurationSerializable) value).serialize());
            return;
        }
        if (!(value instanceof Map)) {
            super.set(path, value);
            return;
        }
        LinkedHashMap map = new LinkedHashMap<>((Map) value);
        for (Object o : map.keySet()) set(path + '.' + o, map.get(o));
    }

    @Deprecated
    public Map<String, Object> getMap(String path) {
        return getMap(path, null);
    }

    @Deprecated
    public Map<String, Object> getMap(String path, Map<String, Object> def) {
        if (isNull(path)) return def;
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        for (String key : getConfigurationSection(path).getKeys(false)) {
            Object value = get(path + '.' + key);
            if (value instanceof ConfigurationSection) {
                map.put(key, getMap(path + '.' + key, def));
                continue;
            }
            map.put(key, value);
        }
        return map;
    }

    public Map<String, Object> convertToMap(ConfigurationSection section) {
        Map<String, Object> result = new LinkedHashMap<>();
        for (String key : section.getKeys(false)) {
            Object object = section.get(key);
            if (object instanceof ConfigurationSection) result.put(key, convertToMap((ConfigurationSection) object));
            else result.put(key, object);
        }
        return result;
    }

    public boolean isMap(String path) {
        return isConfigurationSection(path);
    }

    public String getFancyString(String path) {
        if (isNull(path)) return null;
        return getString(path).replace('&', '§').replace("\\n", "\n");
    }

    public String getFancyString(String path, String[] placeholders, Object[] replacements) {
        String string = getFancyString(path);
        if (string == null) return null;
        for (int i = 0; i < placeholders.length; i++)
            string = string.replace(placeholders[i], replacements[i].toString());
        return string.replace('&', '§').replace("\\n", "\n");
    }

    public void saveDefaultConfig() {
        plugin.saveResource(name, false);
        reloadConfig();
    }

    public World getWorld(String path) {
        return Bukkit.getWorld(getString(path));
    }

    public boolean isWorld(String path) {
        return getWorld(path) != null;
    }

    public ItemBuilder getItemBuilder(String path) {
        Material material = !isNull(path + ".skull-base64")
                ? Material.SKULL_ITEM
                : Material.valueOf(getString(path + ".material").toUpperCase());
        int amount = getInt(path + ".amount", 1);
        short durability = !isNull(path + ".skull-base64")
                ? 3
                : (short) getInt(path + ".durability", 0);
        String skullBase64 = getString(path + ".skull-base64", null);

        ItemBuilder itemBuilder = skullBase64 == null
                ? new ItemBuilder(material, amount, durability)
                : ItemBuilder.getHead(skullBase64, amount);

        Map<Enchantment, Integer> enchantments = new HashMap<>();

        for (Map.Entry<String, Object> entry : getMap(path + ".enchantments", new HashMap<>()).entrySet()) {
            Enchantment enchantment = Enchantment.getByName(entry.getKey().toUpperCase());
            int level = (int) entry.getValue();
            enchantments.put(enchantment, level);
        }

        Set<ItemFlag> flags = new HashSet<>();
        for (String flagName : getStringList(path + ".item-flags"))
            flags.add(ItemFlag.valueOf(flagName.toUpperCase()));

        Map<String, Object> nbtTags;
        Map<String, Object> configTags = getMap(path + ".nbt-tags", new HashMap<>());
        Map<String, Object> fixedTags = new HashMap<>();
        for (Map.Entry<String, Object> entry : configTags.entrySet())
            if (!ItemBuilder.DEFAULT_ITEM_NBT_TAGS.contains(entry.getKey()))
                fixedTags.put(entry.getKey(), entry.getValue());
        nbtTags = fixedTags;

        itemBuilder
                .displayName(getString(path + ".display-name", "").replace('&', '§'))
                .lore(new ListUtil(getStringList(path + ".lore")).replace('&', '§').getList())
                .enchantments(enchantments)
                .glow(getBoolean(path + ".glow"))
                .unbreakable(getBoolean(path + ".unbreakable"))
                .nbtTags(nbtTags)
                .itemFlags(flags);
        return itemBuilder;
    }

    public static WConfig load(JavaPlugin plugin, String name, boolean saveDefault) {
        WConfig config = new WConfig(plugin, name);
        if (!saveDefault) {
            try {
                File file = config.getFile();
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            config.reloadConfig();
            return config;
        }
        config.saveDefaultConfig();
        return config;
    }
}
