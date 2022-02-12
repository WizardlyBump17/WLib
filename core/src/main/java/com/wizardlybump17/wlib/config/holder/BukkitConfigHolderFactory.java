package com.wizardlybump17.wlib.config.holder;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitConfigHolderFactory extends ConfigHolderFactory {

    @Override
    public String getType() {
        return "bukkit";
    }

    @Override
    public ConfigHolder create(String name) {
        return new BukkitConfigHolder((JavaPlugin) Bukkit.getPluginManager().getPlugin(name));
    }
}
