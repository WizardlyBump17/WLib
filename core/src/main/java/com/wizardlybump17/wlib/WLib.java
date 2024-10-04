package com.wizardlybump17.wlib;

import com.wizardlybump17.wlib.adapter.ItemAdapter;
import com.wizardlybump17.wlib.adapter.player.PlayerAdapter;
import com.wizardlybump17.wlib.command.args.ArgsReaderRegistry;
import com.wizardlybump17.wlib.command.reader.*;
import com.wizardlybump17.wlib.config.holder.BukkitConfigHolderFactory;
import com.wizardlybump17.wlib.config.registry.ConfigHandlerRegistry;
import com.wizardlybump17.wlib.config.registry.ConfigHolderFactoryRegistry;
import com.wizardlybump17.wlib.database.DatabaseRegister;
import com.wizardlybump17.wlib.database.model.MySQLDatabaseModel;
import com.wizardlybump17.wlib.database.model.SQLiteDatabaseModel;
import com.wizardlybump17.wlib.inventory.item.InventoryNavigator;
import com.wizardlybump17.wlib.inventory.item.ItemButton;
import com.wizardlybump17.wlib.inventory.paginated.PaginatedInventoryBuilder;
import com.wizardlybump17.wlib.item.ItemBuilder;
import com.wizardlybump17.wlib.item.ItemFilter;
import com.wizardlybump17.wlib.item.handler.model.ItemMetaHandlerModel;
import com.wizardlybump17.wlib.listener.EntityListener;
import com.wizardlybump17.wlib.listener.PlayerListener;
import com.wizardlybump17.wlib.task.SaveControllersTask;
import com.wizardlybump17.wlib.util.bukkit.NumberFormatter;
import com.wizardlybump17.wlib.util.bukkit.config.ConfigSound;
import com.wizardlybump17.wlib.util.bukkit.config.wrapper.potion.PotionDataWrapper;
import com.wizardlybump17.wlib.util.bukkit.config.wrapper.potion.PotionEffectWrapper;
import com.wizardlybump17.wlib.util.bukkit.particle.*;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class WLib extends JavaPlugin {

    private final SaveControllersTask saveControllersTask = new SaveControllersTask(getLogger());

    @Override
    public void onLoad() {
        ItemMetaHandlerModel.initModels();
        initAdapters();
        initSerializables();
        initCommandSystem();

        DatabaseRegister databaseRegister = DatabaseRegister.getInstance();
        databaseRegister.registerDatabaseModel(new MySQLDatabaseModel());
        databaseRegister.registerDatabaseModel(new SQLiteDatabaseModel());

        initConfigs();
    }

    protected void initConfigs() {
        getLogger().info("Initializing configs...");

        ConfigHolderFactoryRegistry.getInstance().put(WLib.class, new BukkitConfigHolderFactory(this));
        ConfigHandlerRegistry.getInstance().register(SaveControllersTask.class);

        getLogger().info("All configs have been initialized!");
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new EntityListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);

        saveControllersTask.runTaskTimer(this, SaveControllersTask.delay, SaveControllersTask.delay);

        getLogger().info("WLib enabled.");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        saveControllersTask.cancel();
    }

    private void initCommandSystem() {
        ArgsReaderRegistry.INSTANCE.add(new PlayerReader());
        ArgsReaderRegistry.INSTANCE.add(new OfflinePlayerReader());
        ArgsReaderRegistry.INSTANCE.add(new EntityTypeArgsReader());
        ArgsReaderRegistry.INSTANCE.add(new MaterialReader());
        ArgsReaderRegistry.INSTANCE.add(new BlockDataArgsReader());
        ArgsReaderRegistry.INSTANCE.add(new MapJsonArgsReader());
        ArgsReaderRegistry.INSTANCE.add(new PotionEffectTypeReader());
        ArgsReaderRegistry.INSTANCE.add(new EnchantmentReader());
    }

    private void initSerializables() {
        ConfigurationSerialization.registerClass(ItemBuilder.class);
        ConfigurationSerialization.registerClass(ItemFilter.class);
        ConfigurationSerialization.registerClass(NumberFormatter.class);
        ConfigurationSerialization.registerClass(PaginatedInventoryBuilder.class);
        ConfigurationSerialization.registerClass(InventoryNavigator.class);
        ConfigurationSerialization.registerClass(ItemButton.class);

        ConfigurationSerialization.registerClass(ParticleSpawner.class);
        ConfigurationSerialization.registerClass(BlockDataWrapper.class);
        ConfigurationSerialization.registerClass(DustOptionsWrapper.class);
        ConfigurationSerialization.registerClass(ItemStackWrapper.class);

        ConfigurationSerialization.registerClass(ConfigSound.class);

        ConfigurationSerialization.registerClass(PotionDataWrapper.class);
        ConfigurationSerialization.registerClass(PotionEffectWrapper.class);
        ConfigurationSerialization.registerClass(DustTransitionWrapper.class);
    }

    private void initAdapters() {
        getLogger().info("Detected server version: " + getServerVersion());
        setupAdapters();
    }

    private void setupAdapters() {
        String version = getServerVersion();
        switch (version) {
            case "1.20.5", "1.20.6" -> {
                ItemAdapter.setInstance(new com.wizardlybump17.wlib.adapter.v1_20_R4.ItemAdapter());
                PlayerAdapter.setInstance(new com.wizardlybump17.wlib.adapter.v1_20_R4.player.PlayerAdapter());
            }
            case "1.21", "1.21.1" -> {
                ItemAdapter.setInstance(new com.wizardlybump17.wlib.adapter.v1_21_R1.ItemAdapter());
                PlayerAdapter.setInstance(new com.wizardlybump17.wlib.adapter.v1_21_R1.player.PlayerAdapter());
            }
            default -> getLogger().severe("The server version (" + version + ") is not supported by WLib yet.");
        }
    }

    public static WLib getInstance() {
        return getPlugin(WLib.class);
    }

    public static @NonNull String getServerVersion() {
        String version = Bukkit.getVersion();
        String mc = "MC: ";
        return version.substring(version.indexOf(mc) + mc.length(), version.length() - 1);
    }
}
