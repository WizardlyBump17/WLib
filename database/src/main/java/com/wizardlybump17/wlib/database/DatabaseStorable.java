package com.wizardlybump17.wlib.database;

import java.util.Map;

public interface DatabaseStorable {

    boolean isInDatabase();

    void setInDatabase(boolean in);

    boolean isDirty();

    void setDirty(boolean dirty);

    boolean isDeleted();

    void setDeleted(boolean deleted);

    /**
     * Called when this object is requested to be saved to the database.
     * The Map given is the map of all the values that are to be saved.
     * The key is the column name, and the value is the value to be saved.
     * @param data The map of data to be saved.
     */
    default void saveToDatabase(Map<String, Object> data) {
    }

    /**
     * Called when this object is requested to be updated to the database.
     * The "where" Map is the map of all the values used to the {@code WHERE} clause.
     * The "data" Map is the map of all the values that are to be updated.
     * @param where The map of data that is used to the {@code WHERE} clause.
     * @param data The map of data to be updated.
     */
    default void updateToDatabase(Map<String, Object> where, Map<String, Object> data) {
    }

    /**
     * Called when this object is requested to be deleted from the database.
     * The Map given is the map of all the values used to the {@code WHERE} clause.
     * The key is the column name, and the value is the value.
     * @param data The map of data that is used to the {@code WHERE} clause.
     */
    default void deleteFromDatabase(Map<String, Object> data) {
    }
}
