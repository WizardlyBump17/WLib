package com.wizardlybump17.wlib;

import com.wizardlybump17.wlib.adapter.NMSAdapter;
import com.wizardlybump17.wlib.adapter.NMSAdapterRegister;
import com.wizardlybump17.wlib.database.DatabaseRegister;
import com.wizardlybump17.wlib.database.MySQLDatabase;
import com.wizardlybump17.wlib.database.SQLiteDatabase;
import com.wizardlybump17.wlib.listener.PlayerListener;
import org.bukkit.Bukkit;

public class WLib extends WPlugin {

    private static final NMSAdapterRegister ADAPTER_REGISTER = NMSAdapterRegister.getInstance();
    private final DatabaseRegister databaseRegister = DatabaseRegister.getInstance();

    @Override
    public void onLoad() {
        databaseRegister.registerDatabaseClass(MySQLDatabase.class);
        databaseRegister.registerDatabaseClass(SQLiteDatabase.class);
    }

    @Override
    public void onEnable() {
        initAdapters();
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        getLogger().info("WLib enabled.");
    }

    private void initAdapters() { //with my language it would be easier :sunglasses:
        try {
            ADAPTER_REGISTER.registerAdapter(new com.wizardlybump17.wlib.adapter.v1_12_R1.NMSAdapter());
        } catch (NoClassDefFoundError ignored) {
        }
        try {
            ADAPTER_REGISTER.registerAdapter(new com.wizardlybump17.wlib.adapter.v1_8_R3.NMSAdapter());
        } catch (NoClassDefFoundError ignored) {
        }
        try {
            ADAPTER_REGISTER.registerAdapter(new com.wizardlybump17.wlib.adapter.v1_13_R2.NMSAdapter());
        } catch (NoClassDefFoundError ignored) {
        }
        try {
            ADAPTER_REGISTER.registerAdapter(new com.wizardlybump17.wlib.adapter.v1_15_R1.NMSAdapter());
        } catch (NoClassDefFoundError ignored) {
        }
        try {
            ADAPTER_REGISTER.registerAdapter(new com.wizardlybump17.wlib.adapter.v1_16_R3.NMSAdapter());
        } catch (NoClassDefFoundError ignored) {
        }
    }

    public static WLib getInstance() {
        return getPlugin(WLib.class);
    }
}
