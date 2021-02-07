package com.wizardlybump17.wlib.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public abstract class WCommand<K extends JavaPlugin> implements CommandExecutor {

    private static CommandMap commandMap;

    static {
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    protected final K plugin;

    public WCommand(K plugin, String commandName) {
        this.plugin = plugin;
        plugin.getCommand(commandName).setExecutor(this);
    }

    public static void unregisterCommand(String commandName) {
        try {
            Command command = commandMap.getCommand(commandName);
            if (command == null) return;
            Field field = commandMap.getClass().getDeclaredField("knownCommands");
            field.setAccessible(true);
            ((Map<String, Command>) field.get(commandMap)).remove(commandName.toLowerCase());
            command.unregister(commandMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registerCommand(Command command) {
        unregisterCommand(command.getName());
        commandMap.register(command.getName(), command);
    }
}
