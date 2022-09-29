package com.wizardlybump17.wlib.database.model;

import com.wizardlybump17.wlib.database.DatabaseHolder;
import com.wizardlybump17.wlib.database.DatabaseModel;
import com.wizardlybump17.wlib.database.SQLiteDatabase;
import com.wizardlybump17.wlib.database.orm.Modifier;

import java.util.Properties;

public class SQLiteDatabaseModel extends DatabaseModel<SQLiteDatabase> {

    public SQLiteDatabaseModel() {
        super("sqlite", "jdbc:sqlite:{database}");
    }

    @Override
    public SQLiteDatabase createDatabase(DatabaseHolder holder, Properties properties) {
        return new SQLiteDatabase(this, properties, holder);
    }

    @Override
    public String getModifierCommand(Modifier modifier) {
        return switch (modifier) {
            case UNIQUE -> "UNIQUE";
            case PRIMARY_KEY -> "PRIMARY KEY";
            case NOT_NULL -> "NOT NULL";
            case AUTO_INCREMENT -> "AUTOINCREMENT";
        };
    }
}
