package com.wizardlybump17.wlib.config;

import com.wizardlybump17.wlib.item.Item;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

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

    public Location getLocation(String path, Location def) {
        Map<String, Object> map = getMap(path);
        if (map == null)
            return def;

        World world = Bukkit.getWorld(map.get("world").toString());
        double x = ((Number) map.get("x")).doubleValue();
        double y = ((Number) map.get("y")).doubleValue();
        double z = ((Number) map.get("z")).doubleValue();

        return new Location(world, x, y, z);
    }

    public Location getLocation(String path) {
        return getLocation(path, null);
    }

    @Override
    public Vector getVector(String path, Vector def) {
        Map<String, Object> map = getMap(path);
        if (map == null)
            return def;

        double x = ((Number) map.get("x")).doubleValue();
        double y = ((Number) map.get("y")).doubleValue();
        double z = ((Number) map.get("z")).doubleValue();

        return new Vector(x, y, z);
    }

    @Override
    public Vector getVector(String path) {
        return getVector(path, null);
    }

    public World getWorld(String path, World def) {
        String name = getString(path);
        if (name == null)
            return def;
        World world = Bukkit.getWorld(name);
        return world == null ? def : world;
    }

    public World getWorld(String path) {
        return getWorld(path, null);
    }

    @Override
    public void set(String path, Object value) {
        if (value instanceof Map) {
            super.set(path, createSection(path, (Map<?, ?>) value));
            return;
        }
        super.set(path, value);
    }

    public Number getNumber(String path, Number def) {
        return getByType(path, def);
    }

    public Number getNumber(String path) {
        return getByType(path);
    }

    @SuppressWarnings("unchecked")
    public <T> T getByType(String path, T def) {
        return (T) get(path, def);
    }

    public <T> T getByType(String path) {
        return getByType(path, null);
    }

    public Map<String, Object> getMap(String path, Map<String, Object> def) {
        Object object = get(path);
        if (!(object instanceof ConfigurationSection)) return def;
        return convertToMap((ConfigurationSection) object);
    }

    public Map<String, Object> getMap(String path) {
        return getMap(path, null);
    }

    @SuppressWarnings("unchecked")
    public <T extends Enum<T>> Map<T, Object> getEnumMap(String path, Class<T> classType, Map<T, Object> def) {
        Object defObject = get(path, def);
        if (!(defObject instanceof ConfigurationSection))
            return def;

        EnumMap<T, Object> map = new EnumMap<>(classType);
        Map<String, Object> originalMap = convertToMap(((ConfigurationSection) defObject));

        Method method;
        try {
            method = classType.getDeclaredMethod("valueOf", String.class);
        } catch (NoSuchMethodException ignored) {
            return def; //???????????????? ^^^^^
        }

        originalMap.forEach((key, value) -> {
            try {
                map.put((T) method.invoke(null, key.toUpperCase()), value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException ignored) { //we don't want a console log for this
            }
        });

        return map;
    }

    public <T extends Enum<T>> Map<T, Object> getEnumMap(String path, Class<T> classType) {
        return getEnumMap(path, classType, null);
    }

        @SuppressWarnings("unchecked")
    public static Map<String, Object> convertToMap(ConfigurationSection section) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (String key : section.getKeys(false)) {
            Object object = section.get(key);
            if (object instanceof ConfigurationSection)
                object = convertToMap((ConfigurationSection) object);
            if (object instanceof List) {
                try {
                    final List<ConfigurationSection> list = (List<ConfigurationSection>) object;
                    List<Map<String, Object>> fixedList = new ArrayList<>(list.size());
                    for (ConfigurationSection configurationSection : list)
                        fixedList.add(convertToMap(configurationSection));
                    object = fixedList;
                } catch (ClassCastException ignored) {
                }
            }
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

    /**
     * Loads the config from the given file path.
     * If saveDefault is true, the config will be saved using {@link JavaPlugin#saveResource(String, boolean)}.
     * If saveDefault is false, the config will be saved using {@link YamlConfiguration#save(File)}
     * @param filePath where this configuration should be loaded from
     * @param plugin the plugin that is using this configuration
     * @param saveDefault if it must save the default config
     * @return the configuration loaded in the given file path
     */
    public static Config load(String filePath, JavaPlugin plugin, boolean saveDefault) {
        Config config = new Config(plugin, filePath, new File(plugin.getDataFolder(), filePath));
        if (saveDefault)
            config.saveDefaultConfig();
        else
            config.saveConfig();

        return config;
    }

    /**
     * Loads the config from the given file path.
     * It will use the {@link Config#load(String, JavaPlugin, boolean)} method with saveDefault = true
     * @param filePath where this configuration should be loaded from
     * @param plugin the plugin that is using this configuration
     * @return the config loaded in the given file path
     */
    public static Config load(String filePath, JavaPlugin plugin) {
        return load(filePath, plugin, true);
    }

    /**
     * Loads the config from the given file.
     * It will use the {@link Config#load(File, JavaPlugin, boolean)} method with saveDefault = true
     * @param file the file that have the config
     * @param plugin the plugin that is using this config
     * @return the config loaded in the given file
     */
    public static Config load(File file, JavaPlugin plugin) {
        return load(file, plugin, true);
    }

    /**
     * Loads the config from the given file.
     * If load is true, the config will be loaded using {@link YamlConfiguration#load(File)}.
     * If load is false, the config will be saved using {@link YamlConfiguration#save(File)}
     * @param file the file that have the config
     * @param plugin the plugin that is using this config
     * @param load if it must load the config or just save it
     * @return the config loaded in the given file
     */
    public static Config load(File file, JavaPlugin plugin, boolean load) {
        final String absolutePath = file.getAbsolutePath();
        final Config config = new Config(
                plugin,
                absolutePath.substring(absolutePath.indexOf(plugin.getDataFolder().getAbsolutePath())),
                file
        );
        if (load)
            config.reloadConfig();
        else
            config.saveConfig();

        return config;
    }
}
