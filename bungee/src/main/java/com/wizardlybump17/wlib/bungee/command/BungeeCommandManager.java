package com.wizardlybump17.wlib.bungee.command;

import com.wizardlybump17.wlib.command.CommandManager;
import com.wizardlybump17.wlib.command.registered.RegisteredCommand;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class BungeeCommandManager extends CommandManager {

    public BungeeCommandManager(BungeeCommandHolder holder) {
        super(holder);
    }

    @Override
    public void registerCommands(@NotNull Object @NotNull ... objects) {
        super.registerCommands(objects);
        for (Object object : objects) {
            for (RegisteredCommand command : getCommands(object)) {
                Plugin plugin = (Plugin) holder.getHandle();
                plugin.getProxy().getPluginManager().registerCommand(plugin, (Command) holder.getCommand(command.getName()).getExecutor());
            }
        }
    }
}
