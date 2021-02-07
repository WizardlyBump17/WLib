package com.wizardlybump17.wlib.inventory.paginated;

import com.wizardlybump17.wlib.inventory.CustomInventory;
import com.wizardlybump17.wlib.inventory.CustomInventoryHolder;
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

    public void openPage(Player player, int page) {
        players.put(player, page);
        inventories.get(page).open(player);
    }

    public void openNextPage(Player player) {
        if (!players.containsKey(player)) players.put(player, 0);
        else if (players.get(player) == inventories.size() - 1) return;
        CustomInventoryHolder holder = inventories.get(players.get(player)).getHolder();
        boolean ignoreCloseEvent = holder.isIgnoreCloseEvent();
        if (ignoreCloseEvent) holder.setIgnoreCloseEvent(true);
        openPage(player, players.get(player) + 1);
        if (ignoreCloseEvent) holder.setIgnoreCloseEvent(false);
    }

    public void openPreviousPage(Player player) {
        if (!players.containsKey(player)) players.put(player, 0);
        else if (players.get(player) == 0) return;
        CustomInventoryHolder holder = inventories.get(players.get(player)).getHolder();
        boolean ignoreCloseEvent = holder.isIgnoreCloseEvent();
        if (ignoreCloseEvent) holder.setIgnoreCloseEvent(true);
        openPage(player, players.get(player) - 1);
        if (ignoreCloseEvent) holder.setIgnoreCloseEvent(false);
    }

    public CustomInventory getOpenedInventory(Player player) {
        return getInventory(getPlayers().get(player));
    }
}
