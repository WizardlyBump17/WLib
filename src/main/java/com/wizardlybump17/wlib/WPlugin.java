package com.wizardlybump17.wlib;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class WPlugin extends JavaPlugin {

    @Override
    public final void onLoad() {
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        for (String message : loadMessage())
            console.sendMessage('[' + getName() + "] " + message);
        load();
    }

    @Override
    public final void onEnable() {
        enable();
        initListeners();
        initCommands();

        ConsoleCommandSender console = Bukkit.getConsoleSender();
        for (String message : enableMessage())
            console.sendMessage('[' + getName() + "] " + message);
    }

    @Override
    public final void onDisable() {
        disable();
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        for (String message : disableMessage())
            console.sendMessage('[' + getName() + "] " + message);
    }

    public void load() {
    }

    public void enable() {
    }

    public void disable() {
    }

    public final void disablePlugin() {
        Bukkit.getPluginManager().disablePlugin(this);
    }

    protected String[] loadMessage() {
        return new String[0];
    }

    protected String[] enableMessage() {
        return new String[0];
    }

    protected String[] disableMessage() {
        return new String[0];
    }

    protected void initListeners() {
    }

    protected void initCommands() {
    }

    public void saveResources(String... files) {
        for (String file : files)
            saveResource(file, false);
    }
}
