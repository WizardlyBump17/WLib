package com.wizardlybump17.wlib.database;

public interface DatabaseStorable {

    boolean isInDatabase();
    void setInDatabase(boolean in);
    boolean isDirty();
    void setDirty(boolean dirty);
    boolean isDeleted();
    void setDeleted(boolean deleted);
}
