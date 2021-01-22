package com.wizardlybump17.wlib.config;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

@Getter
public class WConfig<K extends JavaPlugin> extends YamlConfiguration {

    private final File file;
    private final K plugin;
    private final String name;

    public WConfig(K plugin, String name, boolean saveDefault) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), name);
        this.name = name;
        if(saveDefault) saveDefaultConfig();
        reloadConfig();
        saveConfig();
    }

    public void reloadConfig() {
        try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            Field yamlField = getClass().getDeclaredField("yaml");
            yamlField.setAccessible(true);

            Field newYamlField = config.getClass().getDeclaredField("yaml");
            newYamlField.setAccessible(true);

            yamlField.set(this, newYamlField.get(config));
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
}
