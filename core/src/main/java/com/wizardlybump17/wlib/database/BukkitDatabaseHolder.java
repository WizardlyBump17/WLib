package com.wizardlybump17.wlib.database;

import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@RequiredArgsConstructor
public class BukkitDatabaseHolder implements DatabaseHolder {

    private final JavaPlugin plugin;

    @Override
    public String getName() {
        return plugin.getName();
    }

    @Override
    public File getDataFolder() {
        return plugin.getDataFolder();
    }
}
