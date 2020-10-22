package com.wizardlybump17.wlib.config;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class CustomConfig {

    private final File file;
    private final Plugin plugin;
    private YamlConfiguration yamlConfiguration;

    public CustomConfig(Plugin plugin, String name) {
        this.plugin = plugin;
        file = new File(plugin.getDataFolder(), name.endsWith(".yml") ? name : name + ".yml");
        reloadConfig();
    }

    public CustomConfig(Plugin plugin, String folder, String name) {
        this(plugin, folder + "/" + name);
    }

    public void reloadConfig() {
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public void saveConfig() {
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDefaultConfig() {
        plugin.saveResource(file.getAbsolutePath(), false);
    }

    public boolean getBoolean(String path, boolean def) {
        return yamlConfiguration.getBoolean(path, def);
    }

    public boolean isBoolean(String path) {
        return yamlConfiguration.isBoolean(path);
    }

    public double getDouble(String path) {
        return yamlConfiguration.getDouble(path);
    }

    public double getDouble(String path, double def) {
        return yamlConfiguration.getDouble(path, def);
    }

    public boolean isDouble(String path) {
        return yamlConfiguration.isDouble(path);
    }

    public long getLong(String path) {
        return yamlConfiguration.getLong(path);
    }

    public long getLong(String path, long def) {
        return yamlConfiguration.getLong(path, def);
    }

    public boolean isLong(String path) {
        return yamlConfiguration.isLong(path);
    }

    public List<?> getList(String path) {
        return yamlConfiguration.getList(path);
    }

    public List<?> getList(String path, List<?> def) {
        return yamlConfiguration.getList(path, def);
    }

    public boolean isList(String path) {
        return yamlConfiguration.isList(path);
    }

    public List<String> getStringList(String path) {
        return yamlConfiguration.getStringList(path);
    }

    public List<Integer> getIntegerList(String path) {
        return yamlConfiguration.getIntegerList(path);
    }

    public List<Boolean> getBooleanList(String path) {
        return yamlConfiguration.getBooleanList(path);
    }

    public List<Double> getDoubleList(String path) {
        return yamlConfiguration.getDoubleList(path);
    }

    public List<Float> getFloatList(String path) {
        return yamlConfiguration.getFloatList(path);
    }

    public List<Long> getLongList(String path) {
        return yamlConfiguration.getLongList(path);
    }

    public List<Byte> getByteList(String path) {
        return yamlConfiguration.getByteList(path);
    }

    public List<Character> getCharacterList(String path) {
        return yamlConfiguration.getCharacterList(path);
    }

    public List<Short> getShortList(String path) {
        return yamlConfiguration.getShortList(path);
    }

    public List<Map<?, ?>> getMapList(String path) {
        return yamlConfiguration.getMapList(path);
    }

    public Vector getVector(String path) {
        return yamlConfiguration.getVector(path);
    }

    public Vector getVector(String path, Vector def) {
        return yamlConfiguration.getVector(path, def);
    }

    public boolean isVector(String path) {
        return yamlConfiguration.isVector(path);
    }

    public OfflinePlayer getOfflinePlayer(String path) {
        return yamlConfiguration.getOfflinePlayer(path);
    }

    public OfflinePlayer getOfflinePlayer(String path, OfflinePlayer def) {
        return yamlConfiguration.getOfflinePlayer(path, def);
    }

    public boolean isOfflinePlayer(String path) {
        return yamlConfiguration.isOfflinePlayer(path);
    }

    public ItemStack getItemStack(String path) {
        return yamlConfiguration.getItemStack(path);
    }

    public ItemStack getItemStack(String path, ItemStack def) {
        return yamlConfiguration.getItemStack(path, def);
    }

    public boolean isItemStack(String path) {
        return yamlConfiguration.isItemStack(path);
    }

    public Color getColor(String path) {
        return yamlConfiguration.getColor(path);
    }

    public Color getColor(String path, Color def) {
        return yamlConfiguration.getColor(path, def);
    }

    public boolean isColor(String path) {
        return yamlConfiguration.isColor(path);
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return yamlConfiguration.getConfigurationSection(path);
    }

    public boolean isConfigurationSection(String path) {
        return yamlConfiguration.isConfigurationSection(path);
    }

    public Set<String> getKeys(boolean deep) {
        return yamlConfiguration.getKeys(deep);
    }

    public Map<String, Object> getValues(boolean deep) {
        return yamlConfiguration.getValues(deep);
    }

    public boolean contains(String path) {
        return yamlConfiguration.contains(path);
    }

    public boolean isSet(String path) {
        return yamlConfiguration.isSet(path);
    }

    public String getCurrentPath() {
        return yamlConfiguration.getCurrentPath();
    }

    public String getName() {
        return yamlConfiguration.getName();
    }

    public Configuration getRoot() {
        return yamlConfiguration.getRoot();
    }

    public ConfigurationSection getDefaultSection() {
        return yamlConfiguration.getDefaultSection();
    }

    public void set(String path, Object value) {
        yamlConfiguration.set(path, value);
    }

    public Object get(String path) {
        return yamlConfiguration.get(path);
    }

    public Object get(String path, Object def) {
        return yamlConfiguration.get(path, def);
    }

    public ConfigurationSection createSection(String path) {
        return yamlConfiguration.createSection(path);
    }

    public ConfigurationSection createSection(String path, Map<?, ?> map) {
        return yamlConfiguration.createSection(path, map);
    }

    public String getString(String path) {
        return yamlConfiguration.getString(path);
    }

    public String getString(String path, String def) {
        return yamlConfiguration.getString(path, def);
    }

    public boolean isString(String path) {
        return yamlConfiguration.isString(path);
    }

    public int getInt(String path) {
        return yamlConfiguration.getInt(path);
    }

    public int getInt(String path, int def) {
        return yamlConfiguration.getInt(path, def);
    }

    public boolean isInt(String path) {
        return yamlConfiguration.isInt(path);
    }

    public boolean getBoolean(String path) {
        return yamlConfiguration.getBoolean(path);
    }

    public World getWorld(String path) {
        return Bukkit.getWorld(getString(path));
    }

    public boolean isWorld(String path) {
        return getWorld(path) != null;
    }
}
