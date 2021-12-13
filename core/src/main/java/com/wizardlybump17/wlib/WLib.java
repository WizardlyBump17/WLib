package com.wizardlybump17.wlib;

import com.wizardlybump17.wlib.adapter.NMSAdapterRegister;
import com.wizardlybump17.wlib.adapter.WMaterial;
import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.CommandManager;
import com.wizardlybump17.wlib.command.args.ArgsReaderRegistry;
import com.wizardlybump17.wlib.command.holder.BukkitCommandHolder;
import com.wizardlybump17.wlib.command.reader.OfflinePlayerReader;
import com.wizardlybump17.wlib.command.reader.PlayerReader;
import com.wizardlybump17.wlib.command.sender.PlayerSender;
import com.wizardlybump17.wlib.database.DatabaseRegister;
import com.wizardlybump17.wlib.database.MySQLDatabase;
import com.wizardlybump17.wlib.database.SQLiteDatabase;
import com.wizardlybump17.wlib.inventory.item.InventoryNavigator;
import com.wizardlybump17.wlib.inventory.item.ItemButton;
import com.wizardlybump17.wlib.inventory.paginated.PaginatedInventoryBuilder;
import com.wizardlybump17.wlib.item.Item;
import com.wizardlybump17.wlib.item.ItemFilter;
import com.wizardlybump17.wlib.listener.EntityListener;
import com.wizardlybump17.wlib.util.RandomUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

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

        getLogger().info("WLib enabled.");

        new CommandManager(new BukkitCommandHolder(this)).registerCommands(new Object() {
            @Command(execution = "test")
            public void test(PlayerSender sender) {
                Material[] materials = Arrays.asList(
                        Material.ARROW,
                        Material.BARRIER,
                        Material.BED,
                        Material.BEDROCK
                ).toArray(new Material[0]);

                List<ItemButton> content = new ArrayList<>();

                for (int i = 0; i < 100; i++) {
                    ItemStack itemStack = new ItemStack(RandomUtil.randomElement(materials));
                    AtomicBoolean clicked = new AtomicBoolean(false);
                    content.add(new ItemButton(
                            () -> Item.fromItemStack(itemStack)
                                    .lore("§7Clicked: " + (clicked.get() ? "§aYes" : "§cNo"))
                                    .glow(clicked.get())
                                    .build(),
                            ((event, inventory) -> {
                                clicked.set(!clicked.get());
                                inventory.updateButton(event.getSlot());
                            }
                    )));
                }

                PaginatedInventoryBuilder.create()
                        .title("Test")
                        .shape("#########" +
                                "#xxxxxxx#" +
                                "#xxxxxxx#" +
                                "<#######>")
                        .shapeReplacement('#', new ItemButton(Item.builder().type(WMaterial.BLACK_STAINED_GLASS_PANE).displayName(" ").build()))
                        .nextPage(new InventoryNavigator(Item.builder().type(WMaterial.ARROW).build(), '#'))
                        .previousPage(new InventoryNavigator(Item.builder().type(WMaterial.ARROW).build(), '#'))
                        .content(content)
                        .build()
                        .show(sender.getHandle());
            }
        });
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    private void initCommandSystem() {
        ArgsReaderRegistry.INSTANCE.add(new PlayerReader());
        ArgsReaderRegistry.INSTANCE.add(new OfflinePlayerReader());
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
        try {
            ADAPTER_REGISTER.registerAdapter(new com.wizardlybump17.wlib.adapter.v1_18_R1.NMSAdapter());
            return;
        } catch (NoClassDefFoundError ignored) {
        }
    }

    public static WLib getInstance() {
        return getPlugin(WLib.class);
    }
}
