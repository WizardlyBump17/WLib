package com.wizardlybump17.wlib.database;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.sqlite.JDBC;

import java.io.File;
import java.util.Properties;
import java.util.function.Consumer;

@Getter
public class SQLiteDatabase extends Database {

    private final File file;

    protected SQLiteDatabase(Properties properties, JavaPlugin plugin) {
        super(plugin, properties);
        file = new File(plugin.getDataFolder(), properties.getProperty("database", "database.db"));
    }

    @Override
    public void open(Consumer<Database> callback) {
        new JDBC();
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.open(callback);
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:sqlite:" + file;
    }

    public static String getType() {
        return "sqlite";
    }
}
