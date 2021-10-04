package com.wizardlybump17.wlib.database;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.Properties;
import java.util.function.Consumer;

@Data
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Database {

    private final DatabaseHolder holder;
    protected final Properties properties;
    private Connection connection;

    public boolean isClosed() {
        return connection == null;
    }

    public void open(Consumer<Database> callback) {
        try {
            if (!isClosed()) return;
            connection = DriverManager.getConnection(getJdbcUrl(), properties);
            if (callback != null)
                callback.accept(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void open() {
        open(null);
    }

    public void close() {
        close(null);
    }

    public void close(Consumer<Database> callback) {
        try {
            if (isClosed()) return;
            connection.close();
            connection = null;
            if (callback != null)
                callback.accept(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Database update(String command, Object... replacements) {
        try {
            returnUpdate(command, replacements).close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Executes an update and returns the PreparedStatement
     * @param command the update command
     * @param replacements the replacements to be put in the "?" at the command, if present
     * @return the PreparedStatement
     */
    public PreparedStatement returnUpdate(String command, Object... replacements) {
        if (isClosed()) return null;
        try {
            PreparedStatement statement = connection.prepareStatement(command, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < replacements.length; i++)
                statement.setObject(i + 1, replacements[i]);
            statement.executeUpdate();
            return statement;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Executes a query. Remember to close the ResultSet!
     * @param query the query to be executed
     * @param replacements the replacements to be put in the "?" at the query, if present
     * @return the ResultSet
     */
    public ResultSet query(String query, Object... replacements) {
        if (isClosed()) return null;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            for (int i = 0; i < replacements.length; i++)
                statement.setObject(i + 1, replacements[i]);
            return statement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public abstract String getJdbcUrl();
}
