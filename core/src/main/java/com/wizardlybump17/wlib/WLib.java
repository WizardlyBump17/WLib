package com.wizardlybump17.wlib;

import com.wizardlybump17.wlib.adapter.ItemAdapter;
import com.wizardlybump17.wlib.command.args.ArgsReaderRegistry;
import com.wizardlybump17.wlib.command.reader.EntityTypeArgsReader;
import com.wizardlybump17.wlib.command.reader.OfflinePlayerReader;
import com.wizardlybump17.wlib.command.reader.PlayerReader;
import com.wizardlybump17.wlib.database.DatabaseRegister;
import com.wizardlybump17.wlib.database.MySQLDatabase;
import com.wizardlybump17.wlib.database.SQLiteDatabase;
import com.wizardlybump17.wlib.item.ItemBuilder;
import com.wizardlybump17.wlib.item.ItemFilter;
import com.wizardlybump17.wlib.item.enchantment.GlowEnchantment;
import com.wizardlybump17.wlib.listener.EntityListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class WLib extends JavaPlugin {

    private final DatabaseRegister databaseRegister = DatabaseRegister.getInstance();

    @Override
    public void onLoad() {
        initAdapters();
        initSerializables();
        initCommandSystem();

        GlowEnchantment.register();

        databaseRegister.registerDatabaseClass(MySQLDatabase.class);
        databaseRegister.registerDatabaseClass(SQLiteDatabase.class);
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new EntityListener(), this);

        getLogger().info("WLib enabled.");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    private void initCommandSystem() {
        ArgsReaderRegistry.INSTANCE.add(new PlayerReader());
        ArgsReaderRegistry.INSTANCE.add(new OfflinePlayerReader());
        ArgsReaderRegistry.INSTANCE.add(new EntityTypeArgsReader());
    }

    private void initSerializables() {
        ConfigurationSerialization.registerClass(ItemBuilder.class, "item-builder");
        ConfigurationSerialization.registerClass(ItemFilter.class, "item-filter");
    }

    private void initAdapters() {
        printVersion(selectItemAdapter(), "item");
    }

    private void printVersion(String version, String what) {
        if (version == null)
            getLogger().severe("Could not find " + what + " adapter.");
        else
            getLogger().info(what + " adapter found: " + version);
    }

    private String selectItemAdapter() {
        String version = Bukkit.getServer().getClass().getName().split("\\.")[3];
        return switch (version) {
            case "v1_17_R1" -> {
                ItemAdapter.setInstance(new com.wizardlybump17.wlib.adapter.v1_17_R1.ItemAdapter());
                yield "v1_17_R1";
            }
            case "v1_18_R1" -> {
                ItemAdapter.setInstance(new com.wizardlybump17.wlib.adapter.v1_18_R1.ItemAdapter());
                yield "v1_18_R1";
            }
            case "v1_18_R2" -> {
                ItemAdapter.setInstance(new com.wizardlybump17.wlib.adapter.v1_18_R2.ItemAdapter());
                yield "v1_18_R2";
            }
            case "v1_19_R1" -> {
                ItemAdapter.setInstance(new com.wizardlybump17.wlib.adapter.v1_19_R1.ItemAdapter());
                yield "v1_19_R1";
            }
            default -> null;
        };
    }

    public static WLib getInstance() {
        return getPlugin(WLib.class);
    }
}
