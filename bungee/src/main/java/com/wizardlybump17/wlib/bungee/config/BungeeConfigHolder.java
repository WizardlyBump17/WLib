package com.wizardlybump17.wlib.bungee.config;

import com.wizardlybump17.wlib.config.Configuration;
import com.wizardlybump17.wlib.config.holder.ConfigHolder;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class BungeeConfigHolder implements ConfigHolder {

    private final Plugin plugin;
    private final Map<String, Configuration> configs = new HashMap<>();

    @Override
    public String getName() {
        return plugin.getDescription().getName();
    }

    @Override
    public Configuration getConfig(String name) {
        if (configs.containsKey(name))
            return configs.get(name);

        Configuration config = BungeeConfig.load(name, plugin);
        configs.put(name, config);
        return config;
    }

    @Override
    public Object getHandle() {
        return plugin;
    }
}
