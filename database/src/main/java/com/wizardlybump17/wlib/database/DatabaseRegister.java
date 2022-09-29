package com.wizardlybump17.wlib.database;

import lombok.Getter;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Getter
public final class DatabaseRegister {

    private static DatabaseRegister instance;

    private final Map<String, DatabaseModel<?>> databaseTypes = new HashMap<>();

    public void registerDatabaseModel(DatabaseModel<?> model) {
        databaseTypes.put(model.getType().toLowerCase(), model);
    }

    public Database<?> createDatabase(String propertiesFile, DatabaseHolder holder) {
        Properties properties = new Properties();
        try (FileInputStream stream = new FileInputStream(new File(holder.getDataFolder(), propertiesFile))) {
            properties.load(stream);
            return createDatabase(properties, holder);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Database<?> createDatabase(Properties properties, DatabaseHolder holder) {
        try {
            if (!properties.containsKey("type"))
                throw new NullPointerException("property \"type\" not found");

            String type = properties.getProperty("type").toLowerCase();
            if (!databaseTypes.containsKey(type))
                throw new IllegalArgumentException("invalid database type \"" + type + "\"");

            return databaseTypes.get(type).createDatabase(holder, properties);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DatabaseRegister getInstance() {
        return instance == null ? instance = new DatabaseRegister() : instance;
    }
}
