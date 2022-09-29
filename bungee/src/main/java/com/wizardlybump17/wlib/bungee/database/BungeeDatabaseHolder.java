package com.wizardlybump17.wlib.bungee.database;

import com.wizardlybump17.wlib.database.DatabaseHolder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Getter
public class BungeeDatabaseHolder implements DatabaseHolder<Plugin> {

    private final Plugin plugin;

    @Override
    public String getName() {
        return plugin.getDescription().getName();
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
    public Plugin getHandle() {
        return plugin;
    }
}
