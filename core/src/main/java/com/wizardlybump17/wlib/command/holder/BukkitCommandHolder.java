package com.wizardlybump17.wlib.command.holder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

@RequiredArgsConstructor
@Getter
public class BukkitCommandHolder implements CommandHolder<JavaPlugin> {

    private final JavaPlugin handle;

    public static BukkitCommandHolder of(JavaPlugin plugin) {
        return new BukkitCommandHolder(plugin);
    }

    @Override
    public Command getCommand(String name) {
        return new BukkitCommand(handle.getCommand(name));
    }
}
