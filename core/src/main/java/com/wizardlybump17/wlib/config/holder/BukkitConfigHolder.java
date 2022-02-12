package com.wizardlybump17.wlib.config.holder;

import com.wizardlybump17.wlib.config.Config;
import com.wizardlybump17.wlib.config.Configuration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class BukkitConfigHolder implements ConfigHolder {

    private final JavaPlugin handle;
    private final Map<String, Configuration> configs = new HashMap<>();

    @Override
    public String getName() {
        return handle.getName();
    }

    @Override
    public Configuration getConfig(String name) {
        if (configs.containsKey(name))
            return configs.get(name);

        Configuration configuration = Config.load(name, handle);
        configs.put(name, configuration);
        return configuration;
    }
}
