package com.wizardlybump17.wlib.config;

import lombok.Data;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.LinkedHashMap;
import java.util.Map;

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

    public Map<String, Object> convertToMap(ConfigurationSection section) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (String key : section.getKeys(false)) {
            Object object = section.get(key);
            if (object instanceof ConfigurationSection)
                object = convertToMap((ConfigurationSection) object);
            map.put(key, object);
        }
        return map;
    }

    public String getFancyString(String path, String def) {
        String string = getString(path, def);
        if (string == null)
            return null;
        return def.replace("\\n", "\n").replace('&', '§');
    }

    public String getFancyString(String path) {
        return getFancyString(path, null);
    }

    public static Config load(String filePath, JavaPlugin plugin) {
        return new Config(plugin, filePath, new File(plugin.getDataFolder(), filePath));
    }
}