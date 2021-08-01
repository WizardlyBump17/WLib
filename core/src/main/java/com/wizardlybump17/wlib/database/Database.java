package com.wizardlybump17.wlib.database;

import com.sun.rowset.CachedRowSetImpl;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

import javax.sql.rowset.CachedRowSet;
import java.sql.*;
import java.util.Properties;
import java.util.function.Consumer;

@Data
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Database {

    private final JavaPlugin plugin;
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

    public CachedRowSet query(String query, Object... replacements) {
        if (isClosed()) return null;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < replacements.length; i++)
                statement.setObject(i + 1, replacements[i]);
            CachedRowSet rowSet = new CachedRowSetImpl();
            rowSet.populate(statement.executeQuery());
            return rowSet;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public abstract String getJdbcUrl();
}
