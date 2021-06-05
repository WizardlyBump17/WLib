package com.wizardlybump17.wlib;

import com.wizardlybump17.wlib.database.DatabaseRegister;
import com.wizardlybump17.wlib.database.MySQLDatabase;
import com.wizardlybump17.wlib.database.SQLiteDatabase;
import com.wizardlybump17.wlib.listener.PlayerListener;
import com.wizardlybump17.wlib.adapter.NMSAdapterRegister;
import org.bukkit.Bukkit;

public class WLib extends WPlugin {

    private final DatabaseRegister databaseRegister = DatabaseRegister.getInstance();
    private final NMSAdapterRegister nmsAdapterRegister = NMSAdapterRegister.getInstance();

    @Override
    public void load() {
        try {
            nmsAdapterRegister.registerAdapter(new com.wizardlybump17.wlib.adapter.v1_8_R3.NMSAdapter());
        } catch (NoClassDefFoundError ignored) {}
        try {
            nmsAdapterRegister.registerAdapter(new com.wizardlybump17.wlib.adapter.v_1_16_R3.NMSAdapter());
        } catch (NoClassDefFoundError ignored) {}

        databaseRegister.registerDatabaseClass(MySQLDatabase.class);
        databaseRegister.registerDatabaseClass(SQLiteDatabase.class);
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
