package com.wizardlybump17.wlib.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class Config extends YamlConfiguration {

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
    
    public static Config load(String filePath, JavaPlugin plugin) {
        Config config = new Config();
        try {
            config.load(new File(plugin.getDataFolder(), filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }
}
