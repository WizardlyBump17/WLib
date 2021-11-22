package com.wizardlybump17.wlib.command.holder;

import com.wizardlybump17.wlib.command.BukkitCommandExecutor;
import com.wizardlybump17.wlib.command.CommandManager;
import com.wizardlybump17.wlib.command.RegisteredCommand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class BukkitCommandHolder implements CommandHolder<JavaPlugin> {

    private final Map<String, Command> commands = new HashMap<>();
    private final Map<String, CommandExecutor> executors = new HashMap<>();
    private final JavaPlugin handle;

    public static BukkitCommandHolder of(JavaPlugin plugin) {
        return new BukkitCommandHolder(plugin);
    }

    @Override
    public Command getCommand(String name) {
        if (commands.containsKey(name))
            return commands.get(name);

        final PluginCommand command = handle.getCommand(name);
        BukkitCommand bukkitCommand = new BukkitCommand(command);

        commands.put(name, bukkitCommand);

        return bukkitCommand;
    }

    @Override
    public void onCommandCreate(CommandManager manager, RegisteredCommand command) {
        String name = command.getCommand().execution().split(" ")[0];
        if (executors.containsKey(name))
            return;

        BukkitCommandExecutor executor = new BukkitCommandExecutor(manager);
        getCommand(name).setExecutor(executor);
        executors.put(name, executor);
    }

    @Override
    public void onCommandDelete(CommandManager manager, RegisteredCommand command) {
        executors.remove(command.getCommand().execution().split(" ")[0]);
    }
}
