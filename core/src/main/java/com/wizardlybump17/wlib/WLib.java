package com.wizardlybump17.wlib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wizardlybump17.wlib.command.WLibCommandExecutor;
import com.wizardlybump17.wlib.command.extractor.method.MethodCommandExtractor;
import com.wizardlybump17.wlib.command.extractor.method.factory.OfflinePlayerMethodCommandNodeFactory;
import com.wizardlybump17.wlib.command.extractor.method.factory.PlayerMethodCommandNodeFactory;
import com.wizardlybump17.wlib.command.extractor.method.factory.object.JsonElementMethodCommandNodeFactory;
import com.wizardlybump17.wlib.command.listener.BukkitCommandManagerListener;
import com.wizardlybump17.wlib.command.manager.CommandManager;
import com.wizardlybump17.wlib.command.registry.MethodCommandNodeFactoryRegistry;
import com.wizardlybump17.wlib.command.sender.BukkitCommandSender;
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
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class WLib extends JavaPlugin {

    private final SaveControllersTask saveControllersTask = new SaveControllersTask(getLogger());

    private MethodCommandExtractor methodCommandExtractor;
    private MethodCommandNodeFactoryRegistry methodCommandNodeFactoryRegistry;
    private CommandManager commandManager;
    private WLibCommandExecutor commandExecutor;
    private BukkitCommandManagerListener commandManagerListener;
    private Gson gson;

    @Override
    public void onLoad() {
        gson = new GsonBuilder().create();

        initCommandSystem();
        ItemMetaHandlerModel.initModels();
        initAdapters();
        initSerializables();

        DatabaseRegister databaseRegister = DatabaseRegister.getInstance();
        databaseRegister.registerDatabaseModel(new MySQLDatabaseModel());
        databaseRegister.registerDatabaseModel(new SQLiteDatabaseModel());

        initConfigs();
    }

    private void initCommandSystem() {
        methodCommandNodeFactoryRegistry = new MethodCommandNodeFactoryRegistry();
        methodCommandExtractor = new MethodCommandExtractor(methodCommandNodeFactoryRegistry);

        methodCommandNodeFactoryRegistry.registerDefaults();
        methodCommandNodeFactoryRegistry.addFactory(new OfflinePlayerMethodCommandNodeFactory());
        methodCommandNodeFactoryRegistry.addFactory(new PlayerMethodCommandNodeFactory());
        methodCommandNodeFactoryRegistry.addFactory(new JsonElementMethodCommandNodeFactory(gson));

        commandManager = new CommandManager();

        commandExecutor = new WLibCommandExecutor(commandManager, getLogger());

        commandManagerListener = new BukkitCommandManagerListener(commandExecutor);
        commandManager.addListener(commandManagerListener);
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
        gson = null;

        clearCommandSystem();
        HandlerList.unregisterAll(this);
        saveControllersTask.cancel();
    }

    private void clearCommandSystem() {
        if (methodCommandNodeFactoryRegistry != null)
            methodCommandNodeFactoryRegistry.clear();
        methodCommandNodeFactoryRegistry = null;

        methodCommandExtractor = null;

        if (commandManager != null) {
            commandManager.clear();
            commandManager.clearListeners();
        }
        commandManager = null;

        commandExecutor = null;

        commandManagerListener = null;

        BukkitCommandSender.clearCache();
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
        getLogger().info("Detected server version: " + Bukkit.getMinecraftVersion());
        setupAdapters();
    }

    private void setupAdapters() {
        #if WLIB_INCLUDE_NMS
        String version = Bukkit.getMinecraftVersion();
        switch (version) {
            case "1.20.5", "1.20.6" -> {
                ItemAdapter.setInstance(new com.wizardlybump17.wlib.adapter.v1_20_R4.ItemAdapter());
                PlayerAdapter.setInstance(new com.wizardlybump17.wlib.adapter.v1_20_R4.player.PlayerAdapter());
                AttributeAdapter.setInstance(new com.wizardlybump17.wlib.adapter.v1_20_R4.AttributeAdapter());
                CommandMapAdapter.setInstance(new com.wizardlybump17.wlib.adapter.v1_20_R4.command.CommandMapAdapter());
            }
            case "1.21", "1.21.1" -> {
                ItemAdapter.setInstance(new com.wizardlybump17.wlib.adapter.v1_21_R1.ItemAdapter());
                PlayerAdapter.setInstance(new com.wizardlybump17.wlib.adapter.v1_21_R1.player.PlayerAdapter());
                AttributeAdapter.setInstance(new com.wizardlybump17.wlib.adapter.v1_21_R1.AttributeAdapter());
                CommandMapAdapter.setInstance(new com.wizardlybump17.wlib.adapter.v1_21_R1.command.CommandMapAdapter());
            }
            case "1.21.4" -> {
                ItemAdapter.setInstance(new com.wizardlybump17.wlib.adapter.v1_21_R3.ItemAdapter());
                PlayerAdapter.setInstance(new com.wizardlybump17.wlib.adapter.v1_21_R3.player.PlayerAdapter());
                AttributeAdapter.setInstance(new com.wizardlybump17.wlib.adapter.v1_21_R3.AttributeAdapter());
                CommandMapAdapter.setInstance(new com.wizardlybump17.wlib.adapter.v1_21_R3.command.CommandMapAdapter());
            }
            case "1.21.6", "1.21.7" -> {
                ItemAdapter.setInstance(new com.wizardlybump17.wlib.adapter.v1_21_R5.ItemAdapter());
                PlayerAdapter.setInstance(new com.wizardlybump17.wlib.adapter.v1_21_R5.player.PlayerAdapter());
                AttributeAdapter.setInstance(new com.wizardlybump17.wlib.adapter.v1_21_R5.AttributeAdapter());
                CommandMapAdapter.setInstance(new com.wizardlybump17.wlib.adapter.v1_21_R5.command.CommandMapAdapter());
            }
            default -> getLogger().severe("The server version (" + version + ") is not supported by WLib yet.");
        }
        #endif
    }

    public static WLib getInstance() {
        return getPlugin(WLib.class);
    }

    public MethodCommandExtractor getMethodCommandExtractor() {
        return methodCommandExtractor;
    }

    public MethodCommandNodeFactoryRegistry getMethodCommandNodeFactoryRegistry() {
        return methodCommandNodeFactoryRegistry;
    }

    public WLibCommandExecutor getCommandExecutor() {
        return commandExecutor;
    }

    public BukkitCommandManagerListener getCommandManagerListener() {
        return commandManagerListener;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public Gson getGson() {
        return gson;
    }

    public SaveControllersTask getSaveControllersTask() {
        return saveControllersTask;
    }
}
