package com.wizardlybump17.wlib;

import com.wizardlybump17.wlib.inventory.item.ItemButton;
import com.wizardlybump17.wlib.inventory.paginated.PaginatedInventoryBuilder;
import com.wizardlybump17.wlib.item.ItemBuilder;
import com.wizardlybump17.wlib.listener.InventoryClickListener;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WLib extends JavaPlugin {

    @Override
    public void onEnable() {
        initEvents();
    }

    private void initEvents() {
        new InventoryClickListener(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        PaginatedInventoryBuilder paginatedInventory = new PaginatedInventoryBuilder("§cTest", 9 * 6,
                "#########" +
                        "#xxxxxxx#" +
                        "#xxxxxxx#" +
                        "#xxxxxxx#" +
                        "#xxxxxxx#" +
                        "<#######>"
        );
        List<ItemButton> itemButtons = new ArrayList<>();
        Random random = new Random();
        Material[] materials = new Material[]{Material.ACACIA_DOOR_ITEM, Material.BEDROCK, Material.COBBLESTONE};
        for (int i = 0; i < 300; i++) {
            Material material = materials[random.nextInt(materials.length)];
            itemButtons.add(new ItemButton(new ItemStack(material), event -> event.getWhoClicked().sendMessage("§aYou clicked in " + material)));
        }
        paginatedInventory
                .nextPageItemStack(
                        new ItemBuilder(Material.ARROW)
                                .displayName("§aNext page")
                                .build())
                .previousPageItemStack(
                        new ItemBuilder(Material.ARROW)
                                .displayName("§aPrevious page")
                                .build())
                .border(new ItemButton(
                        new ItemBuilder(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getData())
                                .displayName(" ")
                                .build()
                ))
                .items(itemButtons);
        paginatedInventory.build().open((Player) sender, 0);
        return true;
    }
}
