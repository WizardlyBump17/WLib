package com.wizardlybump17.wlib.database;

import com.mysql.jdbc.Driver;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.Properties;
import java.util.function.Consumer;

public class MySQLDatabase extends Database {

    protected MySQLDatabase(Properties properties, JavaPlugin plugin) {
        super(plugin, properties);
    }

    @Override
    public final String getJdbcUrl() {
        return "jdbc:mysql://" + properties.getProperty("host", "localhost") + ':' +
                properties.getProperty("port", "3306") + '/' +
                properties.getProperty("database", getPlugin().getName().toLowerCase());
    }

    @Override
    public void open(Consumer<Database> callback) {
        try {
            new Driver();
            super.open(callback);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getType() {
        return "mysql";
    }
}
