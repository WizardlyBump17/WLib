package com.wizardlybump17.wlib.database;

import lombok.Getter;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.*;

@Getter
public final class DatabaseRegister {

    private static DatabaseRegister instance;

    private final Map<String, Class<? extends Database>> databaseTypes = new HashMap<>();
    private final List<Database> databases = new ArrayList<>();

    public void registerDatabaseClass(Class<? extends Database> clazz) {
        try {
            Method method = clazz.getDeclaredMethod("getType");
            databaseTypes.put(method.invoke(null).toString().toLowerCase(), clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Database createDatabase(String propertiesFile, DatabaseHolder holder) {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(holder.getDataFolder(), propertiesFile)));
            return createDatabase(properties, holder);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Database createDatabase(Properties properties, DatabaseHolder holder) {
        try {
            if (!properties.containsKey("type"))
                throw new NullPointerException("property \"type\" not found");
            String type = properties.getProperty("type").toLowerCase();
            if (!databaseTypes.containsKey(type))
                throw new IllegalArgumentException("invalid database type \"" + type + "\"");
            return databaseTypes.get(type).getDeclaredConstructor(Properties.class, DatabaseHolder.class).newInstance(properties, holder);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DatabaseRegister getInstance() {
        return instance == null ? instance = new DatabaseRegister() : instance;
    }
}
