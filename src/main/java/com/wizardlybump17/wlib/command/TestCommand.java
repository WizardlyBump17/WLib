package com.wizardlybump17.wlib.command;

import com.wizardlybump17.wlib.inventory.ItemButton;
import com.wizardlybump17.wlib.inventory.paginated.PaginatedInventory;
import com.wizardlybump17.wlib.item.Item;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestCommand implements CommandExecutor {
    
    private static final Random RANDOM = new Random();
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        int items = Integer.parseInt(args[0]);

        List<ItemButton> content = new ArrayList<>();
        for (int i = 0; i < items; i++) {
            Material material = Material.values()[RANDOM.nextInt(Material.values().length)];
            content.add(new ItemButton(
                    Item.builder().type(material).amount(1).displayName("§f" + material.name()).build(),
                    event -> event.getWhoClicked().sendMessage("§f" + material.name())));
        }
        PaginatedInventory.builder()
                .title("Test {page}")
                .shape("#########" +
                        "#xxxxxxx#" +
                        "#xxxxxxx#" +
                        "#xxxxxxx#" +
                        "#xxxxxxx#" + //TODO fix the inventories
                        "<#######>")
                .nextPage(Item.builder().type(Material.ARROW).amount(1).displayName("§aNext page").build())
                .previousPage(Item.builder().type(Material.ARROW).amount(1).displayName("§aPrevious page").build())
                .shapeReplacement('#', new ItemButton(Item.builder()
                        .type(Material.STAINED_GLASS_PANE)
                        .amount(1)
                        .durability(DyeColor.BLACK.getData())
                        .displayName(" ")
                        .build()))
                .content(content)
                .build()
                .show(player, 0);
        return false;
    }
}
