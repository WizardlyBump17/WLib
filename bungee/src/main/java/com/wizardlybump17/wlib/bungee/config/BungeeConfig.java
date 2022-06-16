package com.wizardlybump17.wlib.bungee.config;

import com.wizardlybump17.wlib.bungee.util.ColorUtil;
import com.wizardlybump17.wlib.bungee.util.PluginUtil;
import com.wizardlybump17.wlib.config.Configuration;
import com.wizardlybump17.wlib.config.Path;
import com.wizardlybump17.wlib.util.ArrayUtils;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class BungeeConfig implements Configuration<net.md_5.bungee.config.Configuration> {

    @NotNull
    private net.md_5.bungee.config.Configuration handle;
    private final String name;
    private final File file;
    private final Plugin plugin;

    @Override
    public Object get(String path, Object def) {
        Object o = handle.get(path, def);
        if (o instanceof String)
            return ColorUtil.format(o.toString().replace("\\n", "\n"));
        if (o instanceof net.md_5.bungee.config.Configuration)
            return toMap((net.md_5.bungee.config.Configuration) o);
        return o;
    }

    @Override
    public Object get(String path) {
        return get(path, (Object) null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getByType(String path, T def) {
        return (T) get(path, def);
    }

    @Override
    public <T> T getByType(String path) {
        return getByType(path, null);
    }

    @Override
    public Object get(String path, Object def, Class<?> type) {
        return get(path, def);
    }

    @Override
    public Object get(String path, Class<?> type) {
        return get(path);
    }

    @Override
    public Object get(String path, Object def, Class<?> type, Path requester) {
        Object o = get(path, def, type);
        if (o == null)
            o = def;

        if (ArrayUtils.contains(requester.options(), "fancy") && o instanceof String)
            return ColorUtil.format(o.toString().replace("\\n", "\n"));

        if (o instanceof Number)
            return fixNumber(o, type);

        return o;
    }

    /**
     * For {@link Path}. If the path is a number, it will be converted to the correct type.
     * @param object the original object
     * @param type the type to convert to
     * @return the converted object
     */
    private Number fixNumber(Object object, Class<?> type) {
        if (type == byte.class || type == Byte.class)
            return ((Number) object).byteValue();
        if (type == short.class || type == Short.class)
            return ((Number) object).shortValue();
        if (type == int.class || type == Integer.class)
            return ((Number) object).intValue();
        if (type == long.class || type == Long.class)
            return ((Number) object).longValue();
        if (type == float.class || type == Float.class)
            return ((Number) object).floatValue();
        if (type == double.class || type == Double.class)
            return ((Number) object).doubleValue();
        if (type == BigInteger.class)
            return new BigInteger(object.toString());
        if (type == BigDecimal.class)
            return new BigDecimal(object.toString());
        return 0;
    }

    @Override
    public Object get(String path, Class<?> type, Path requester) {
        return get(path, null, type, requester);
    }

    @Override
    public void set(String path, Object value) {
        handle.set(path, value);
    }

    @Override
    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(handle, file);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save config to " + file);
            e.printStackTrace();
        }
    }

    @Override
    public void saveDefaultConfig() {
        PluginUtil.saveResource(plugin, name, file);
        reloadConfig();
    }

    @Override
    public void reloadConfig() {
        try {
            handle = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not load config from " + file);
            e.printStackTrace();
        }
    }

    @Override
    @NotNull
    public net.md_5.bungee.config.Configuration getHandle() {
        return handle;
    }

    public Map<String, Object> asMap() {
        return toMap(handle);
    }

    public static Map<String, Object> toMap(net.md_5.bungee.config.Configuration configuration) {
        Map<String, Object> map = new HashMap<>();
        for (String key : configuration.getKeys()) {
            Object value = configuration.get(key);
            if (value instanceof net.md_5.bungee.config.Configuration)
                value = toMap((net.md_5.bungee.config.Configuration) value);
            map.put(key, value);
        }
        return map;
    }

    /**
     * Loads a config based on the name of the file.<br>
     * It will load the file from {@link Plugin#getDataFolder()}
     * @param name the file name
     * @param plugin the plugin
     * @param saveDefault if it should generate the default config
     * @return the loaded config
     */
    public static BungeeConfig load(String name, Plugin plugin, boolean saveDefault) {
        File file = new File(plugin.getDataFolder(), name);
        net.md_5.bungee.config.Configuration handle;
        if (file.exists())
            try {
                handle = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            } catch (IOException e) {
                plugin.getLogger().severe("Could not load config from " + file);
                e.printStackTrace();
                return null;
            }
        else
            handle = new net.md_5.bungee.config.Configuration();

        BungeeConfig config = new BungeeConfig(handle, name, file, plugin);
        if (saveDefault)
            config.saveDefaultConfig();
        return config;
    }

    /**
     * Loads a config based on the name of the file.<br>
     * It will load the file from {@link Plugin#getDataFolder()}
     * @param name the file name
     * @param plugin the plugin
     * @return the loaded config
     */
    public static BungeeConfig load(String name, Plugin plugin) {
        return load(name, plugin, true);
    }

    /**
     * Loads a config based on the given file
     * @param file the file
     * @param plugin the plugin
     * @return the loaded config
     */
    public static BungeeConfig load(File file, Plugin plugin) {
        net.md_5.bungee.config.Configuration handle;
        try {
            handle = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not load config from " + file);
            e.printStackTrace();
            return null;
        }

        return new BungeeConfig(handle, file.getName(), file, plugin);
    }
}
