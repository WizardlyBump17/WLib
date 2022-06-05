package com.wizardlybump17.wlib.bungee;

import com.wizardlybump17.wlib.database.DatabaseRegister;
import com.wizardlybump17.wlib.database.MySQLDatabase;
import com.wizardlybump17.wlib.database.SQLiteDatabase;
import net.md_5.bungee.api.plugin.Plugin;

public class WLib extends Plugin {

    @Override
    public void onLoad() {
        DatabaseRegister databaseRegister = DatabaseRegister.getInstance();
        databaseRegister.registerDatabaseClass(MySQLDatabase.class);
        databaseRegister.registerDatabaseClass(SQLiteDatabase.class);
    }

    @Override
    public void onDisable() {
    }
}
