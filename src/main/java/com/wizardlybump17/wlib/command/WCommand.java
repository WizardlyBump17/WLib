package com.wizardlybump17.wlib.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class WCommand implements CommandExecutor {

    protected final JavaPlugin plugin;

    public WCommand(JavaPlugin plugin, String commandName) {
        this.plugin = plugin;
        plugin.getCommand(commandName).setExecutor(this);
        plugin.getLogger().info("Registered the command executor of " + commandName + " command");
    }
}
