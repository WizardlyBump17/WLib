package com.wizardlybump17.wlib.config.registry;

import com.wizardlybump17.wlib.config.ConfigInfo;
import com.wizardlybump17.wlib.config.handler.ConfigHandler;
import com.wizardlybump17.wlib.config.holder.ConfigHolder;
import com.wizardlybump17.wlib.config.holder.ConfigHolderFactory;
import com.wizardlybump17.wlib.object.Registry;
import lombok.NonNull;

import java.util.Iterator;

/**
 * This class is responsible to store the {@link ConfigHandler}s
 */
public class ConfigHandlerRegistry extends Registry<Class<?>, ConfigHandler> {

    private static ConfigHandlerRegistry instance;

    /**
     * This will register the class to the registry
     *
     * @param clazz the class to register
     */
    public ConfigHandler register(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(ConfigInfo.class))
            return null;

        ConfigInfo info = clazz.getAnnotation(ConfigInfo.class);
        ConfigHolderFactory factory = ConfigHolderFactoryRegistry.getInstance().get(info.holderType());
        if (factory == null)
            throw new NullPointerException("ConfigHolderFactory not found for " + info.holderType());

        ConfigHolder holder = factory.create(info.holderType());

        ConfigHandler handler = new ConfigHandler(clazz, holder.getConfig(info.name()), info.holderType(), info.saveDefault());
        handler.init();
        handler.loadFields();
        put(clazz, handler);
        return handler;
    }

    /**
     * This will reload all configs from that holder
     *
     * @param holder the holder who is holding the configs
     */
    public void reloadAll(Class<?> holder) {
        for (ConfigHandler config : getMap().values())
            if (holder == config.getHolder())
                config.reload();
    }

    /**
     * Initializes all configs from that holder
     *
     * @param holder the holder who is holding the configs
     */
    public void initAll(Class<?> holder) {
        for (ConfigHandler config : getMap().values())
            if (holder == config.getHolder())
                config.init();
    }

    /**
     * <p>
     * Saves all {@link com.wizardlybump17.wlib.config.Configuration}s with the given holder class.
     * </p>
     *
     * @param holder the holder with the configs
     */
    public void saveAll(@NonNull Class<?> holder) {
        for (ConfigHandler config : getMap().values())
            if (holder == config.getHolder())
                config.save();
    }

    /**
     * <p>Unregisters all configurations from that holder.</p>
     *
     * @param holder the holder who is holding the configurations
     */
    public void unregisterAll(@NonNull Class<?> holder) {
        Iterator<ConfigHandler> iterator = map.values().iterator();
        while (iterator.hasNext()) {
            ConfigHandler config = iterator.next();
            if (holder != config.getHolder())
                continue;

            config.save();
            iterator.remove();
        }
    }

    /**
     * <p>
     *     Removes all configurations associated with the given holder.
     *     It will not save the configurations.
     * </p>
     * @param holder the holder who is holding the configurations
     */
    public void removeAll(@NonNull Class<?> holder) {
        map.values().removeIf(config -> holder == config.getHolder());
    }

    public static ConfigHandlerRegistry getInstance() {
        return instance == null ? instance = new ConfigHandlerRegistry() : instance;
    }
}
