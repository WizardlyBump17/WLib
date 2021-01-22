package com.wizardlybump17.wlib.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class WListener<K extends JavaPlugin> implements Listener {

    protected K plugin;

    public WListener(K plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}
