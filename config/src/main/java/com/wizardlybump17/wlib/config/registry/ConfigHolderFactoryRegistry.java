package com.wizardlybump17.wlib.config.registry;

import com.wizardlybump17.wlib.config.holder.ConfigHolderFactory;
import com.wizardlybump17.wlib.object.Registry;

public class ConfigHolderFactoryRegistry extends Registry<Class<?>, ConfigHolderFactory> {

    private static ConfigHolderFactoryRegistry instance;

    public static ConfigHolderFactoryRegistry getInstance() {
        return instance == null ? instance = new ConfigHolderFactoryRegistry() : instance;
    }
}
