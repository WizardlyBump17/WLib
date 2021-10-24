package com.wizardlybump17.wlib;

import com.wizardlybump17.wlib.adapter.NMSAdapterRegister;
import com.wizardlybump17.wlib.database.DatabaseRegister;
import com.wizardlybump17.wlib.database.MySQLDatabase;
import com.wizardlybump17.wlib.database.SQLiteDatabase;
import com.wizardlybump17.wlib.listener.EntityListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.HandlerList;
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
        initSerializables();
        Bukkit.getPluginManager().registerEvents(new EntityListener(), this);
        getLogger().info("WLib enabled.");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    private void initSerializables() {
        ConfigurationSerialization.registerClass(com.wizardlybump17.wlib.item.Item.ItemBuilder.class, "item-builder");
    }

    private void initAdapters() { //with my language it would be easier :sunglasses:
        try {
            ADAPTER_REGISTER.registerAdapter(new com.wizardlybump17.wlib.adapter.v1_12_R1.NMSAdapter());
        } catch (NoClassDefFoundError ignored) {
            try {
                ADAPTER_REGISTER.registerAdapter(new com.wizardlybump17.wlib.adapter.v1_8_R3.NMSAdapter());
            } catch (NoClassDefFoundError ignored1) {
                try {
                    ADAPTER_REGISTER.registerAdapter(new com.wizardlybump17.wlib.adapter.v1_13_R2.NMSAdapter());
                } catch (NoClassDefFoundError ignored2) {
                    try {
                        ADAPTER_REGISTER.registerAdapter(new com.wizardlybump17.wlib.adapter.v1_15_R1.NMSAdapter());
                    } catch (NoClassDefFoundError ignored3) {
                        try {
                            ADAPTER_REGISTER.registerAdapter(new com.wizardlybump17.wlib.adapter.v1_16_R3.NMSAdapter());
                        } catch (NoClassDefFoundError ignored4) {
                            try {
                                ADAPTER_REGISTER.registerAdapter(new com.wizardlybump17.wlib.adapter.v1_17_R1.NMSAdapter());
                            } catch (NoClassDefFoundError ignored5) {
                            }
                        }
                    }
                }
            }
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
