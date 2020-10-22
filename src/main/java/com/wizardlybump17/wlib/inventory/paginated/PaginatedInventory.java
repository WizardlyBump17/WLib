package com.wizardlybump17.wlib.inventory.paginated;

import com.wizardlybump17.wlib.inventory.CustomInventory;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class PaginatedInventory {

    private final List<CustomInventory> inventories;
    private final Map<Player, Integer> players = new HashMap<>();

    public PaginatedInventory(List<CustomInventory> inventories) {
        this.inventories = inventories;
    }

    public PaginatedInventory() {
        this(new ArrayList<>());
    }

    public void addInventory(CustomInventory inventory) {
        inventories.add(inventory);
    }

    public void removeInventory(int page) {
        inventories.remove(page);
    }

    public CustomInventory getInventory(int page) {
        return inventories.get(page);
    }

    public void open(Player player, int page) {
        players.put(player, page);
        inventories.get(page).open(player);
    }

    public void nextPage(Player player) {
        if (!players.containsKey(player)) players.put(player, 0);
        if (players.get(player) == inventories.size() - 1) return;
        open(player, players.get(player) + 1);
    }

    public void previousPage(Player player) {
        if (!players.containsKey(player)) players.put(player, 0);
        if (players.get(player) == 0) return;
        open(player, players.get(player) - 1);
    }

    public CustomInventory getOpenedInventory(Player player) {
        return getInventory(getPlayers().get(player));
    }
}
