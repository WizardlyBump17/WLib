package com.wizardlybump17.wlib;

import com.wizardlybump17.wlib.adapter.NMSAdapterRegister;
import com.wizardlybump17.wlib.command.BukkitCommandManagerListener;
import com.wizardlybump17.wlib.command.CommandManager;
import com.wizardlybump17.wlib.command.args.ArgsReaderRegistry;
import com.wizardlybump17.wlib.command.reader.OfflinePlayerReader;
import com.wizardlybump17.wlib.command.reader.PlayerReader;
import com.wizardlybump17.wlib.database.DatabaseRegister;
import com.wizardlybump17.wlib.database.MySQLDatabase;
import com.wizardlybump17.wlib.database.SQLiteDatabase;
import com.wizardlybump17.wlib.item.Item;
import com.wizardlybump17.wlib.item.ItemFilter;
import com.wizardlybump17.wlib.listener.EntityListener;
import com.wizardlybump17.wlib.listener.PluginListener;
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
        initCommandSystem();

        Bukkit.getPluginManager().registerEvents(new EntityListener(), this);
        Bukkit.getPluginManager().registerEvents(new PluginListener(), this);

        getLogger().info("WLib enabled.");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        if (CommandManager.getCreateListener() instanceof BukkitCommandManagerListener)
            CommandManager.getCreateListener().getExecutors().clear();
    }

    private void initCommandSystem() {
        ArgsReaderRegistry.INSTANCE.add(new PlayerReader());
        ArgsReaderRegistry.INSTANCE.add(new OfflinePlayerReader());

        CommandManager.setCreateListener(new BukkitCommandManagerListener());
    }

    private void initSerializables() {
        ConfigurationSerialization.registerClass(Item.ItemBuilder.class, "item-builder");
        ConfigurationSerialization.registerClass(ItemFilter.class, "item-filter");
    }

    private void initAdapters() { //with my language it would be easier :sunglasses:
        selectAdapter();
        if (ADAPTER_REGISTER.current() == null)
            getLogger().severe("No NMS adapter found for the current version (" + Bukkit.getServer().getClass().getName() + ")!!!");
        else
            getLogger().info("Loaded NMS adapter for version " + ADAPTER_REGISTER.current().getTargetVersion());
    }

    private void selectAdapter() {
        try {
            ADAPTER_REGISTER.registerAdapter(new com.wizardlybump17.wlib.adapter.v1_8_R3.NMSAdapter());
            return;
        } catch (NoClassDefFoundError ignored) {
        }
        try {
            ADAPTER_REGISTER.registerAdapter(new com.wizardlybump17.wlib.adapter.v1_12_R1.NMSAdapter());
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
    }

    public static WLib getInstance() {
        return getPlugin(WLib.class);
    }
}
