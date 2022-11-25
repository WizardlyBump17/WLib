package com.wizardlybump17.wlib.database.model;

import com.wizardlybump17.wlib.database.DatabaseHolder;
import com.wizardlybump17.wlib.database.DatabaseModel;
import com.wizardlybump17.wlib.database.MySQLDatabase;
import com.wizardlybump17.wlib.database.annotation.Modifier;

import java.util.Properties;

public class MySQLDatabaseModel extends DatabaseModel<MySQLDatabase> {

    public MySQLDatabaseModel() {
        super("mysql", "jdbc:mysql://{host}:{port}/{database}");
    }

    @Override
    public MySQLDatabase createDatabase(DatabaseHolder holder, Properties properties) {
        return new MySQLDatabase(this, properties, holder);
    }

    @Override
    public String getModifierCommand(Modifier modifier) {
        return switch (modifier) {
            case UNIQUE -> "UNIQUE";
            case PRIMARY_KEY -> "PRIMARY KEY";
            case NOT_NULL -> "NOT NULL";
            case AUTO_INCREMENT -> "AUTO_INCREMENT";
        };
    }
}
