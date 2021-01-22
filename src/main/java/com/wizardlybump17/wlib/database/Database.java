package com.wizardlybump17.wlib.database;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Getter
public abstract class Database<K extends JavaPlugin> {

    private final K plugin;
    private Connection connection;

    public Database(K plugin) {
        this.plugin = plugin;
    }

    public void openConnection(String user, String password) {
        try {
            closeConnection();
            connection = DriverManager.getConnection(getUrl(), user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openConnection() {
        try {
            closeConnection();
            connection = DriverManager.getConnection(getUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if (isClosed()) return;
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isClosed() {
        try {
            return connection != null && connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    public PreparedStatement query(String command, Object... replacements) {
        if (isClosed()) return null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(command);
            if (replacements != null)
                for (int i = 0; i < replacements.length; i++)
                    preparedStatement.setObject(i + 1, replacements[i]);
            return preparedStatement;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(String command, Object... replacements) {
        if (isClosed()) return;
        try (PreparedStatement preparedStatement = connection.prepareStatement(command)) {
            if (replacements != null)
                for (int i = 0; i < replacements.length; i++)
                    preparedStatement.setObject(i + 1, replacements[i]);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public abstract String getUrl();
}
