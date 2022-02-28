package com.wizardlybump17.wlib.config.holder;

import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class BukkitConfigHolderFactory extends ConfigHolderFactory {

    @Nullable
    private final JavaPlugin plugin;

    public BukkitConfigHolderFactory() {
        this(null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ConfigHolder create(Class<?> type) {
        if (plugin != null)
            return new BukkitConfigHolder(plugin);

        if (!JavaPlugin.class.isAssignableFrom(type))
            throw new IllegalArgumentException("Class " + type.getName() + " is not a JavaPlugin");

        JavaPlugin plugin = JavaPlugin.getPlugin((Class<? extends JavaPlugin>) type);
        return new BukkitConfigHolder(plugin);
    }
}
