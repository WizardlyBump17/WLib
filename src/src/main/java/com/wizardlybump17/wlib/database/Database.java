package com.wizardlybump17.wlib.database;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface Database {

    void openConnection();

    Connection getConnection();

    default boolean isClosed() {
        try {
            return getConnection() == null || getConnection().isClosed();
        } catch (Exception exception) {
            exception.printStackTrace();
            return true;
        }
    }

    void closeConnection();

    PreparedStatement query(String query, Object... replacements);

    void update(String command, Object... replacements);

    JavaPlugin getPlugin();
}
