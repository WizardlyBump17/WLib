package com.wizardlybump17.wlib.command.listener;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.WLibCommandExecutor;
import com.wizardlybump17.wlib.command.bukkit.BukkitCommand;
import com.wizardlybump17.wlib.command.bukkit.InternalBukkitCommand;
import com.wizardlybump17.wlib.command.manager.CommandManager;
import com.wizardlybump17.wlib.command.manager.listener.CommandManagerListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.logging.Level;

public class BukkitCommandManagerListener implements CommandManagerListener {

    private final @NotNull WLibCommandExecutor commandExecutor;

    public BukkitCommandManagerListener(@NotNull WLibCommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    @Override
    public void onRegister(@NotNull String identifier, @NotNull Command command, @Nullable Object holder, @NotNull CommandManager manager) {
        if (!(holder instanceof JavaPlugin plugin))
            return;

        if (command instanceof BukkitCommand bukkitCommand) {
            InternalBukkitCommand internalCommand = bukkitCommand.getInternalCommand();
            internalCommand.setExecutor(commandExecutor);

            Bukkit.getCommandMap().register(command.getName(), identifier, internalCommand);
        } else {
            PluginCommand pluginCommand = plugin.getCommand(command.getName());
            if (pluginCommand == null) {
                plugin.getLogger().log(Level.WARNING, "Command not found on plugin.yml while trying to register it to WLib: " + command.getName());
                return;
            }

            pluginCommand.setExecutor(commandExecutor);
        }
    }

    @Override
    public void onClear(@NotNull CommandManager manager) {
    public void onPreClear(@NotNull CommandManager manager) {
        });
    }

    @Override
    public void onPostClear(@NotNull CommandManager manager) {
    }

    @Override
    public void onUnregister(@NotNull String identifier, @NotNull Command command, @Nullable Object holder, @NotNull CommandManager manager) {
        if (!(holder instanceof JavaPlugin plugin))
            return;

        if (command instanceof BukkitCommand bukkitCommand) {
            bukkitCommand.getInternalCommand().setExecutor(null);

            Map<String, org.bukkit.command.Command> knownCommands = Bukkit.getCommandMap().getKnownCommands();
            knownCommands.remove(identifier + CommandManager.SEPARATOR + command.getName());
            knownCommands.remove(command.getName());
        } else {
            PluginCommand pluginCommand = plugin.getCommand(command.getName());
            if (pluginCommand == null)
                return;

            pluginCommand.setExecutor(null);
        }
    }

    public @NotNull WLibCommandExecutor getCommandExecutor() {
        return commandExecutor;
    }
}
