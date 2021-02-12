package com.wizardlybump17.wlib.config;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

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
        if (!(value instanceof Map)) {
            super.set(path, value);
            return;
        }
        LinkedHashMap map = new LinkedHashMap<>((Map) value);
        for (Object o : map.keySet()) set(path + '.' + o, map.get(o));
    }

    public Map<String, Object> getMap(String path) {
        return getMap(path, null);
    }

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

    public boolean isMap(String path) {
        return getMap(path) != null;
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
