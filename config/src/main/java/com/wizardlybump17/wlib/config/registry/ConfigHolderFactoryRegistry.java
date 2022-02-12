package com.wizardlybump17.wlib.config.registry;

import com.wizardlybump17.wlib.config.holder.ConfigHolderFactory;
import com.wizardlybump17.wlib.object.Registry;

public class ConfigHolderFactoryRegistry extends Registry<String, ConfigHolderFactory> {

    private static ConfigHolderFactoryRegistry instance;

    @Override
    public void put(String key, ConfigHolderFactory value) {
        super.put(key.toLowerCase(), value);
    }

    public static ConfigHolderFactoryRegistry getInstance() {
        return instance == null ? instance = new ConfigHolderFactoryRegistry() : instance;
    }
}
