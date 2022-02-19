package com.wizardlybump17.wlib.config.registry;

import com.wizardlybump17.wlib.config.ConfigInfo;
import com.wizardlybump17.wlib.config.handler.ConfigHandler;
import com.wizardlybump17.wlib.config.holder.ConfigHolder;
import com.wizardlybump17.wlib.config.holder.ConfigHolderFactory;
import com.wizardlybump17.wlib.object.Registry;

/**
 * This class is responsible to store the {@link ConfigHandler}s
 */
public class ConfigHandlerRegistry extends Registry<Class<?>, ConfigHandler> {

    private static ConfigHandlerRegistry instance;

    /**
     * This will register the class to the registry
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

        ConfigHandler handler = new ConfigHandler(clazz, holder.getConfig(info.name()));
        handler.reload();
        put(clazz, handler);
        return handler;
    }

    public static ConfigHandlerRegistry getInstance() {
        return instance == null ? instance = new ConfigHandlerRegistry() : instance;
    }
}
