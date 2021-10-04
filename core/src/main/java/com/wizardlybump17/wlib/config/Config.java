package com.wizardlybump17.wlib.config;

import com.wizardlybump17.wlib.item.Item;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = false)
@Data
public class Config extends YamlConfiguration {

    private final JavaPlugin plugin;
    private final String name;
    private final File file;
    private boolean loaded;
    
    public void saveDefaultConfig() {
        plugin.saveResource(name, false);
        reloadConfig();
    }

    public void saveConfig() {
        try {
            save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        try {
            load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void set(String path, Object value) {
        if (value instanceof Map) {
            super.set(path, createSection(path, (Map<?, ?>) value));
            return;
        }
        if (value instanceof ConfigurationSerializable) {
            set(path, ((ConfigurationSerializable) value).serialize());
            return;
        }
        super.set(path, value);
    }

    public Map<String, Object> getMap(String path, Map<String, Object> def) {
        Object object = get(path);
        if (!(object instanceof ConfigurationSection)) return def;
        return convertToMap((ConfigurationSection) object);
    }

    public Map<String, Object> getMap(String path) {
        return getMap(path, null);
    }

    public static Map<String, Object> convertToMap(ConfigurationSection section) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (String key : section.getKeys(false)) {
            Object object = section.get(key);
            if (object instanceof ConfigurationSection)
                object = convertToMap((ConfigurationSection) object);
            map.put(key, object);
        }
        return map;
    }

    public Map<String, Object> asMap() {
        return convertToMap(this);
    }

    public String getFancyString(String path, String[] placeholders, Object[] replacements) {
        String string = getFancyString(path);
        if (string == null)
            return null;
        for (int i = 0; i < placeholders.length; i++)
            string = string.replace('{' + placeholders[i] + '}', replacements[i] == null ? "null" : replacements[i].toString());
        return string;
    }

    public String getFancyString(String path, String def) {
        String string = getString(path, def);
        if (string == null)
            return def;
        return string.replace("\\n", "\n").replace('&', '§');
    }

    public String getFancyString(String path) {
        return getFancyString(path, null);
    }

    public Item.ItemBuilder getItemBuilder(String path, Item.ItemBuilder def) {
        Object object = get(path);
        if (!(object instanceof ConfigurationSection)) return def;
        return Item.deserialize(convertToMap((ConfigurationSection) object));
    }

    public Item.ItemBuilder getItemBuilder(String path) {
        return getItemBuilder(path, null);
    }

    public static Config load(String filePath, JavaPlugin plugin) {
        return new Config(plugin, filePath, new File(plugin.getDataFolder(), filePath));
    }

    public static Config load(File file, JavaPlugin plugin) {
        final String absolutePath = file.getAbsolutePath();
        final Config config = new Config(
                plugin,
                absolutePath.substring(absolutePath.indexOf(plugin.getDataFolder().getAbsolutePath())),
                file
        );

        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return config;
    }
}
