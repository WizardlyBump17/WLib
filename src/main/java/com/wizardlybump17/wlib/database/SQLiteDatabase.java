package com.wizardlybump17.wlib.database;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class SQLiteDatabase implements Database {

    private final File file;
    private final JavaPlugin plugin;
    private Connection connection;

    public SQLiteDatabase(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), fileName);
        if (file.getParentFile().mkdir())
            plugin.getLogger().info("Created " + file.getParentFile().getName() + " directory");
        else
            plugin.getLogger().info("The directory " + file.getParentFile().getName() + " wasn't created, probably it already exists");
        try {
            if (file.createNewFile()) plugin.getLogger().info("Created " + fileName + " file");
            else plugin.getLogger().info("The file " + fileName + " wasn't created, probably it already exists");
        } catch (IOException e) {
            plugin.getLogger().severe("Error while creating file " + fileName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void openConnection() {
        if (!isClosed()) return;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + file);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void closeConnection() {
        if (isClosed()) return;
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PreparedStatement query(String query, Object... replacements) {
        if (isClosed()) return null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            if (replacements != null)
                for (int i = 0; i < replacements.length; i++)
                    preparedStatement.setObject(i + 1, replacements[i]);

            return preparedStatement;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(String command, Object... replacements) {
        if (isClosed()) return;

        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(command)) {

            if (replacements != null)
                for (int i = 0; i < replacements.length; i++)
                    preparedStatement.setObject(i + 1, replacements[i]);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JavaPlugin getPlugin() {
        return plugin;
    }
}
