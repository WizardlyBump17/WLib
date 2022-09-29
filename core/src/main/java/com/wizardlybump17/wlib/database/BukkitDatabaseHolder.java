package com.wizardlybump17.wlib.database;

import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class BukkitDatabaseHolder implements DatabaseHolder<JavaPlugin> {

    private final JavaPlugin plugin;

    @Override
    public String getName() {
        return plugin.getName();
    }

    @Override
    public File getDataFolder() {
        return plugin.getDataFolder();
    }

    @Override
    public Logger getLogger() {
        return plugin.getLogger();
    }

    @Override
    public JavaPlugin getHandle() {
        return plugin;
    }
}
