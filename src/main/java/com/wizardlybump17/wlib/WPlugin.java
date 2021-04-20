package com.wizardlybump17.wlib;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

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

    protected String[] loadMessage() {
        return new String[0];
    }

    protected String[] enableMessage() {
        return new String[0];
    }

    protected String[] disableMessage() {
        return new String[0];
    }

    public void saveResources(String... files) {
        for (String file : files)
            saveResource(file, false);
    }
}
