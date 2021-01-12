package com.wizardlybump17.wlib.database;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;

public class MySQLDatabase implements Database {

    private final String host, database, user, password;
    private final int port;
    private final JavaPlugin plugin;
    private Connection connection;

    public MySQLDatabase(JavaPlugin plugin, String host, int port, String database, String user, String password) {
        this.plugin = plugin;
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    @Override
    public void openConnection() {
        if (!isClosed()) return;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
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

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    @Override
    public JavaPlugin getPlugin() {
        return plugin;
    }
}
