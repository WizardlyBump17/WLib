package com.wizardlybump17.wlib.command;

import com.wizardlybump17.wlib.command.holder.BukkitCommandHolder;
import com.wizardlybump17.wlib.command.holder.CommandExecutor;
import com.wizardlybump17.wlib.command.holder.CommandHolder;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

@Getter
public class BukkitCommandManagerListener implements CommandManagerListener {

    private final Set<JavaPlugin> plugins = new HashSet<>();
    private final Map<String, CommandExecutor> executors = new HashMap<>();
    private final Map<JavaPlugin, Set<CommandExecutor>> pluginCommands = new HashMap<>();

    @Override
    public void onCommandCreate(CommandManager manager, RegisteredCommand command) {
        final String commandName = command.getCommand().execution().split(" ")[0];
        if (!executors.containsKey(commandName)) {
            final BukkitCommandExecutor executor = new BukkitCommandExecutor(manager);
            manager.getHolder().getCommand(commandName).setExecutor(executor);
            executors.put(commandName, executor);

            final JavaPlugin handle = ((BukkitCommandHolder) manager.getHolder()).getHandle();
            plugins.add(handle);
            pluginCommands.putIfAbsent(handle, new HashSet<>());
            pluginCommands.get(handle).add(executor);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCommandDelete(CommandManager manager, RegisteredCommand command) {
        final String commandName = command.getCommand().execution().split(" ")[0];
        executors.remove(commandName);

        final CommandHolder<JavaPlugin> holder = manager.getHolder();
        final JavaPlugin handle = holder.getHandle();
        pluginCommands.get(handle).remove(holder.getCommand(commandName).getExecutor());

        if (pluginCommands.get(handle).isEmpty())
            pluginCommands.remove(handle);
    }

    @Override
    public Collection<CommandExecutor> getExecutors() {
        return executors.values();
    }
}
