package com.wizardlybump17.wlib.listener;

import com.wizardlybump17.wlib.command.CommandManager;
import com.wizardlybump17.wlib.command.holder.BukkitCommandHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginListener implements Listener {

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        final CommandManager manager = CommandManager.MANAGERS.get(BukkitCommandHolder.of((JavaPlugin) event.getPlugin()));
        if (manager != null)
            manager.unregisterCommands();
    }
}
