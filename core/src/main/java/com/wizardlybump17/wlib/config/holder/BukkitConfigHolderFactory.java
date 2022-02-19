package com.wizardlybump17.wlib.config.holder;

import org.bukkit.plugin.java.JavaPlugin;

public class BukkitConfigHolderFactory extends ConfigHolderFactory {

    @SuppressWarnings("unchecked")
    @Override
    public ConfigHolder create(Class<?> type) {
        if (!JavaPlugin.class.isAssignableFrom(type))
            throw new IllegalArgumentException("Class " + type.getName() + " is not a JavaPlugin");

        JavaPlugin plugin = JavaPlugin.getPlugin((Class<? extends JavaPlugin>) type);
        return new BukkitConfigHolder(plugin);
    }
}
