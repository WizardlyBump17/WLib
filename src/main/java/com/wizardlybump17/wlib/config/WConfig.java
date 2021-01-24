package com.wizardlybump17.wlib.config;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

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
                config.getFile().getParentFile().mkdirs();
                config.getFile().createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else config.saveDefaultConfig();
        config.reloadConfig();
        return config;
    }
}
