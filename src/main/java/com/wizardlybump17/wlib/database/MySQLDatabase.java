package com.wizardlybump17.wlib.database;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Properties;

public class MySQLDatabase extends Database {

    protected MySQLDatabase(Properties properties, JavaPlugin plugin) {
        super(plugin, properties);
    }

    @Override
    public final String getJdbcUrl() {
        return "jdbc:mysql://" + properties.getProperty("host", "localhost") + ':' +
                properties.getProperty("port", "3306") + '/' +
                properties.getProperty("database", "wlib");
    }

    public static String getType() {
        return "mysql";
    }
}
