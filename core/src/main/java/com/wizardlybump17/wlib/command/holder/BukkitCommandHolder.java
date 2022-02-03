package com.wizardlybump17.wlib.command.holder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class BukkitCommandHolder implements CommandHolder<JavaPlugin> {

    private final Map<String, Command> commands = new HashMap<>();
    private final JavaPlugin handle;

    public static BukkitCommandHolder of(JavaPlugin plugin) {
        return new BukkitCommandHolder(plugin);
    }

    @Override
    public Command getCommand(String name) {
        BukkitCommand bukkitCommand = new BukkitCommand(handle.getCommand(name));
        commands.put(name, bukkitCommand);
        return bukkitCommand;
    }
}
