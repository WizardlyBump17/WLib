package com.wizardlybump17.wlib.database;

import lombok.NonNull;

import java.util.Collections;
import java.util.Map;

public interface DatabaseStorable {

    boolean isInDatabase();

    void setInDatabase(boolean in);

    boolean isDirty();

    void setDirty(boolean dirty);

    boolean isDeleted();

    void setDeleted(boolean deleted);

    /**
     * <p>Called when this object is requested to be saved to the database.</p>
     * <p>The {@link Map} returned is used to get the column names and values to be saved.</p>
     * @param database the database that is requesting the method
     * @param table the table where the object is to be saved
     * @return the {@link Map} of data to be saved
     * @since 1.5.1
     */
    @NonNull
    default Map<String, Object> saveToDatabase(Database<?> database, String table) {
        return Collections.emptyMap();
    }

    /**
     * <p>Called when this object is requested to be updated to the database.</p>
     * <p>The {@link Map} returned is used to get the column names and values to be updated.</p>
     * @param database the database that is requesting the method
     * @param table the table where the object is to be updated
     * @param where the {@link Map} of data that is used to the {@code WHERE} clause. Initially an empty {@link java.util.HashMap}
     * @return the {@link Map} of data to be updated
     * @since 1.5.1
     */
    @NonNull
    default Map<String, Object> updateToDatabase(Database<?> database, String table, Map<String, Object> where) {
        return Collections.emptyMap();
    }

    /**
     * <p>Called when this object is requested to be deleted from the database.</p>
     * <p>The {@link Map} returned is used to get the column names and values used to the {@code WHERE} clause.</p>
     * @param database the database that is requesting the method
     * @param table the table where the object is to be deleted
     * @return the {@link Map} of data that is used to the {@code WHERE} clause
     * @since 1.5.1
     */
    @NonNull
    default Map<String, Object> deleteFromDatabase(Database<?> database, String table) {
        return Collections.emptyMap();
    }
}
