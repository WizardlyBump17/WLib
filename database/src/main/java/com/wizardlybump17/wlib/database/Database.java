package com.wizardlybump17.wlib.database;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.*;
import java.util.function.Consumer;

@Data
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Database<M extends DatabaseModel<?>> {

    private final M model;
    private final DatabaseHolder holder;
    protected final Properties properties;
    protected Connection connection;

    public boolean isClosed() {
        return connection == null;
    }

    public void open(Consumer<Database<M>> callback) {
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

    public void close(Consumer<Database<M>> callback) {
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

    public Database<M> update(String command, Object... replacements) {
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

    /**
     * Saves the given object to the database.
     * If the object is null, nothing happens.
     * If the object is deleted, it will delete the object.
     * If the object is in the database, it will try to update the object.
     * If the object is not in the database, it will insert the object to the database.
     * Note that it will update the {@link DatabaseStorable#isDirty()} and {@link DatabaseStorable#isInDatabase()} flags if needed.
     * It won't check if the table is valid
     * @param object the object to be saved
     * @param table the table to be saved to
     */
    public void save(DatabaseStorable object, String table) {
        if (object == null)
            return;

        if (object.isDeleted()) {
            delete(object, table);
            return;
        }

        if (object.isInDatabase()) {
            if (object.isDirty())
                update(object, table);
            return;
        }

        insert(object, table);
    }

    /**
     * Called when the object is requested to be updated
     * @param object the object to be updated
     * @param table the table to be updated
     */
    protected void update(DatabaseStorable object, String table) {
        if (object == null)
            return;

        StringBuilder builder = new StringBuilder("UPDATE " + table + " SET ");
        Map<String, Object> where = new HashMap<>();
        Map<String, Object> data = object.updateToDatabase(this, table, where);

        if (data.isEmpty())
            return;

        for (Map.Entry<String, Object> entry : data.entrySet())
            builder.append(entry.getKey()).append(" = ?, ");
        builder.delete(builder.length() - 2, builder.length());

        if (!where.isEmpty()) {
            builder.append(" WHERE ");
            for (Map.Entry<String, Object> entry : where.entrySet())
                builder.append(entry.getKey()).append(" = ? AND ");
            builder.delete(builder.length() - 5, builder.length());
        }

        builder.append(";");

        object.setDirty(false);

        List<Object> list = new ArrayList<>(data.values());
        list.addAll(where.values());

        update(builder.toString(), list.toArray());
    }

    /**
     * Called when the object is requested to be deleted
     * @param object the object to be deleted
     * @param table the table to be deleted from
     */
    protected void delete(DatabaseStorable object, String table) {
        if (object == null)
            return;

        if (!object.isInDatabase())
            return;

        Map<String, Object> where = object.deleteFromDatabase(this, table);
        if (where.isEmpty())
            return;

        StringBuilder builder = new StringBuilder("DELETE FROM " + table + " WHERE ");
        for (Map.Entry<String, Object> entry : where.entrySet())
            builder.append(entry.getKey()).append(" = ? AND ");
        builder.delete(builder.length() - 5, builder.length());

        builder.append(";");

        update(builder.toString(), where.values().toArray());
    }

    /**
     * Called when the object is requested to be inserted
     * @param object the object to be inserted
     * @param table the table to be inserted to
     */
    protected void insert(DatabaseStorable object, String table) {
        if (object == null)
            return;

        Map<String, Object> data = object.saveToDatabase(this, table);
        if (data.isEmpty())
            return;

        StringBuilder builder = new StringBuilder("INSERT INTO " + table + " (");
        for (String s : data.keySet())
            builder.append(s).append(", ");
        builder.delete(builder.length() - 2, builder.length());

        builder.append(") VALUES (");
        builder.append("?, ".repeat(data.size()));
        builder.delete(builder.length() - 2, builder.length());

        builder.append(");");

        object.setInDatabase(true);
        object.setDirty(false);

        update(builder.toString(), data.values().toArray());
    }

    /**
     * <p>Saves the given objects to the database.</p>
     * <p>The default implementation calls the {@link #save(DatabaseStorable, String)} for each object and uses the same table</p>
     * @param table the table to be saved to
     * @param objects the objects to be saved
     */
    public void save(@NonNull String table, @NonNull DatabaseStorable... objects) {
        for (DatabaseStorable object : objects)
            save(object, table);
    }

    /**
     * <p>Saves the given objects to the database.</p>
     * <p>The default implementation calls the {@link #save(DatabaseStorable, String)} for each object and uses the same table</p>
     * @param objects the objects to be saved
     * @param table the table to be saved to
     */
    public void save(@NonNull Iterable<DatabaseStorable> objects, @NonNull String table) {
        for (DatabaseStorable object : objects)
            save(object, table);
    }
}
