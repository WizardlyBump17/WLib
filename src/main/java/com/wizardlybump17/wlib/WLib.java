package com.wizardlybump17.wlib;

import com.wizardlybump17.wlib.database.DatabaseRegister;
import com.wizardlybump17.wlib.database.MySQLDatabase;
import com.wizardlybump17.wlib.database.SQLiteDatabase;
import com.wizardlybump17.wlib.listener.PlayerListener;
import com.wizardlybump17.wlib.reflection.ReflectionAdapterRegister;
import com.wizardlybump17.wlib.reflection.v1_8_R3.ReflectionAdapter;
import org.bukkit.Bukkit;

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
    public void enable() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    protected String[] enableMessage() {
        return new String[]{"§aThank you for using §lWLib§r §c§l<3"};
    }

    public static WLib getInstance() {
        return getPlugin(WLib.class);
    }
}
