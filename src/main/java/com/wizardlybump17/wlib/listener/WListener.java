package com.wizardlybump17.wlib.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class WListener implements Listener {

    protected JavaPlugin plugin;

    public WListener(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        plugin.getLogger().info("Registered the events in " + getClass().getName() + " class");
    }
}
