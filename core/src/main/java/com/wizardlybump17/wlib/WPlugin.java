package com.wizardlybump17.wlib;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

@Deprecated
public abstract class WPlugin extends JavaPlugin { //useless but useful

    @Override
    public void onLoad() {
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        for (String message : loadMessage())
            console.sendMessage('[' + getName() + "] " + message);
        load();
    }

    @Override
    public void onEnable() {
        enable();
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        for (String message : enableMessage())
            console.sendMessage('[' + getName() + "] " + message);
    }

    @Override
    public void onDisable() {
        disable();
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        for (String message : disableMessage())
            console.sendMessage('[' + getName() + "] " + message);
    }

    @Deprecated
    public void load() {
    }

    @Deprecated
    public void enable() {
    }

    @Deprecated
    public void disable() {
    }

    @Deprecated
    protected String[] loadMessage() {
        return new String[0];
    }

    @Deprecated
    protected String[] enableMessage() {
        return new String[0];
    }

    @Deprecated
    protected String[] disableMessage() {
        return new String[0];
    }

    protected void saveResources(String... files) {
        for (String file : files)
            saveResource(file, false);
    }
}
