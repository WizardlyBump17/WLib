package com.wizardlybump17.wlib.command.holder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class BukkitCommandHolder implements CommandHolder<JavaPlugin> {

    private static final Map<JavaPlugin, BukkitCommandHolder> cache = new HashMap<>();

    private final JavaPlugin handle;

    @Override
    public Command getCommand(String name) {
        final PluginCommand command = handle.getCommand(name);
        return new BukkitCommand(command);
    }

    public static BukkitCommandHolder of(JavaPlugin handle) {
        cache.putIfAbsent(handle, new BukkitCommandHolder(handle));
        return cache.get(handle);
    }

    public static void clearCache() {
        cache.clear();
    }

    public static void removeFromCache(JavaPlugin plugin) {
        cache.remove(plugin);
    }
}
