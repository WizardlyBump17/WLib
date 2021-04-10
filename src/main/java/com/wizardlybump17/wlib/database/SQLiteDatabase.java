package com.wizardlybump17.wlib.database;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Properties;

@Getter
public class SQLiteDatabase extends Database {

    private final File file;

    protected SQLiteDatabase(Properties properties, JavaPlugin plugin) {
        super(plugin, properties);
        file = new File(plugin.getDataFolder(), properties.getProperty("database", "database.db"));
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:sqlite:" + file;
    }

    public static String getType() {
        return "sqlite";
    }
}
