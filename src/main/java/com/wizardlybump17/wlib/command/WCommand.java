package com.wizardlybump17.wlib.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class WCommand<K extends JavaPlugin> implements CommandExecutor {

    protected final K plugin;

    public WCommand(K plugin, String commandName) {
        this.plugin = plugin;
        plugin.getCommand(commandName).setExecutor(this);
    }
}
