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
    public void onLoad() {
        String version = null;
        try {
            nmsAdapterRegister.registerAdapter(new com.wizardlybump17.wlib.adapter.v1_8_R3.NMSAdapter());
            version = "1.8.8";
        } catch (NoClassDefFoundError ignored) {}
        try {
            nmsAdapterRegister.registerAdapter(new com.wizardlybump17.wlib.adapter.v1_13_R2.NMSAdapter());
            version = "1.13.1";
        } catch (NoClassDefFoundError ignored) {}
        try {
            nmsAdapterRegister.registerAdapter(new com.wizardlybump17.wlib.adapter.v1_15_R1.NMSAdapter());
            version = "1.15.2";
        } catch (NoClassDefFoundError ignored) {}
        try {
            nmsAdapterRegister.registerAdapter(new com.wizardlybump17.wlib.adapter.v_1_16_R3.NMSAdapter());
            version = "1.16.5";
        } catch (NoClassDefFoundError ignored) {}
        if (version == null)
            getLogger().warning("Could not found a NMS adapter available for your current CraftBukkit version. Errors can happen");
        else
            getLogger().info("Loaded version " + version + " for the NMS adapter");

        databaseRegister.registerDatabaseClass(MySQLDatabase.class);
        databaseRegister.registerDatabaseClass(SQLiteDatabase.class);
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        getLogger().info("WLib enabled.");
    }

    public static WLib getInstance() {
        return getPlugin(WLib.class);
    }
}
