package com.wizardlybump17.wlib.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public abstract class CustomListener implements Listener {

    public CustomListener(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}
