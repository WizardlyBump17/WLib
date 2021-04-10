package com.wizardlybump17.wlib;

import com.wizardlybump17.wlib.command.ItemCommand;
import com.wizardlybump17.wlib.database.DatabaseRegister;
import com.wizardlybump17.wlib.database.MySQLDatabase;
import com.wizardlybump17.wlib.database.SQLiteDatabase;
import com.wizardlybump17.wlib.reflection.ReflectionAdapterRegister;
import com.wizardlybump17.wlib.reflection.v1_8_R3.ReflectionAdapter;

public class WLib extends WPlugin {

    private final DatabaseRegister databaseRegister = DatabaseRegister.getInstance();
    private final ReflectionAdapterRegister reflectionAdapterRegister = ReflectionAdapterRegister.getInstance();

    @Override
    public void load() {
        databaseRegister.registerDatabaseClass(MySQLDatabase.class);
        databaseRegister.registerDatabaseClass(SQLiteDatabase.class);

        reflectionAdapterRegister.registerAdapter(new ReflectionAdapter());
    }

    @Override
    protected String[] enableMessage() {
        return new String[]{"§aThank you for using §lWLib§r §c§l<3"};
    }

    @Override
    protected void initCommands() {
        getCommand("item").setExecutor(new ItemCommand());
    }
}
