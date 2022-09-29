package com.wizardlybump17.wlib.bungee;

import com.wizardlybump17.wlib.database.DatabaseRegister;
import com.wizardlybump17.wlib.database.model.MySQLDatabaseModel;
import com.wizardlybump17.wlib.database.model.SQLiteDatabaseModel;
import net.md_5.bungee.api.plugin.Plugin;

public class WLib extends Plugin {

    @Override
    public void onLoad() {
        DatabaseRegister databaseRegister = DatabaseRegister.getInstance();
        databaseRegister.registerDatabaseModel(new MySQLDatabaseModel());
        databaseRegister.registerDatabaseModel(new SQLiteDatabaseModel());
    }

    @Override
    public void onDisable() {
    }
}
