package com.wizardlybump17.wlib.inventory.paginated;

import com.wizardlybump17.wlib.inventory.CustomInventory;
import com.wizardlybump17.wlib.inventory.CustomInventoryHolder;
import com.wizardlybump17.wlib.inventory.ItemButton;
import lombok.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class PaginatedInventory {
    
    private final List<CustomInventory> inventories = new ArrayList<>();
    private final Map<Player, Integer> playerPages = new HashMap<>();

    public void addInventory(CustomInventory inventory) {
        inventories.add(inventory);
    }

    public void show(Player player, int page) {
        page = page < 0 ? 0 : page >= inventories.size() ? inventories.size() - 1 : page;
        playerPages.put(player, page);
        player.openInventory(inventories.get(page).getBukkitInventory());
    }

    public void showNextPage(Player player) {
        show(player, playerPages.getOrDefault(player, -1) + 1);
    }

    public void showPreviousPage(Player player) {
        show(player, playerPages.getOrDefault(player, 1) - 1);
    }
}
