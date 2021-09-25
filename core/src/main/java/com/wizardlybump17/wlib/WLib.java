package com.wizardlybump17.wlib;

import com.wizardlybump17.wlib.adapter.NMSAdapterRegister;
import com.wizardlybump17.wlib.database.DatabaseRegister;
import com.wizardlybump17.wlib.database.MySQLDatabase;
import com.wizardlybump17.wlib.database.SQLiteDatabase;
import com.wizardlybump17.wlib.listener.EntityListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class WLib extends JavaPlugin {

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
        Bukkit.getPluginManager().registerEvents(new EntityListener(), this);
        getLogger().info("WLib enabled.");
    }

    private void initAdapters() { //with my language it would be easier :sunglasses:
        try {
            ADAPTER_REGISTER.registerAdapter(new com.wizardlybump17.wlib.adapter.v1_12_R1.NMSAdapter());
            return;
        } catch (NoClassDefFoundError ignored) {
        }
        try {
            ADAPTER_REGISTER.registerAdapter(new com.wizardlybump17.wlib.adapter.v1_8_R3.NMSAdapter());
            return;
        } catch (NoClassDefFoundError ignored) {
        }
        try {
            ADAPTER_REGISTER.registerAdapter(new com.wizardlybump17.wlib.adapter.v1_13_R2.NMSAdapter());
            return;
        } catch (NoClassDefFoundError ignored) {
        }
        try {
            ADAPTER_REGISTER.registerAdapter(new com.wizardlybump17.wlib.adapter.v1_15_R1.NMSAdapter());
            return;
        } catch (NoClassDefFoundError ignored) {
        }
        try {
            ADAPTER_REGISTER.registerAdapter(new com.wizardlybump17.wlib.adapter.v1_16_R3.NMSAdapter());
            return;
        } catch (NoClassDefFoundError ignored) {
        }
        try {
            ADAPTER_REGISTER.registerAdapter(new com.wizardlybump17.wlib.adapter.v1_17_R1.NMSAdapter());
            return;
        } catch (NoClassDefFoundError ignored) {
        }

        if (ADAPTER_REGISTER.current() == null)
            getLogger().severe("No NMS adapter found for the current version!!!");
        else
            getLogger().info("Loaded NMS adapter for version " + ADAPTER_REGISTER.current().getTargetVersion());
    }

    public static WLib getInstance() {
        return getPlugin(WLib.class);
    }
}
