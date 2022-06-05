package com.wizardlybump17.wlib.bungee.config;

import com.wizardlybump17.wlib.config.holder.ConfigHolder;
import com.wizardlybump17.wlib.config.holder.ConfigHolderFactory;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class BungeeConfigHolderFactory extends ConfigHolderFactory {

    @Nullable
    private final Plugin plugin;

    public BungeeConfigHolderFactory() {
        this(null);
    }

    @Override
    public ConfigHolder create(Class<?> clazz) {
        if (plugin != null)
            return new BungeeConfigHolder(plugin);

        if (!Plugin.class.isAssignableFrom(clazz))
            throw new IllegalArgumentException(clazz.getName() + " is not a " + Plugin.class.getName());

        for (Plugin plugin : ProxyServer.getInstance().getPluginManager().getPlugins())
            if (plugin.getClass() == clazz)
                return new BungeeConfigHolder(plugin);

        throw new IllegalArgumentException(clazz.getName() + " is not loaded");
    }
}
