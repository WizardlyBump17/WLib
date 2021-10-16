package com.wizardlybump17.wlib.inventory;

import com.wizardlybump17.wlib.inventory.paginated.PaginatedInventory;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class InventoryCache {

    private final Map<Inventory, PaginatedInventory> inventories = new HashMap<>();

    public void add(PaginatedInventory inventory) {
        for (CustomInventory customInventory : inventory.getInventories())
            inventories.put(customInventory.getBukkitInventory(), inventory);
    }

    public void remove(PaginatedInventory inventory) {
        for (CustomInventory customInventory : inventory.getInventories())
            inventories.remove(customInventory.getBukkitInventory());
    }

    public PaginatedInventory get(Inventory inventory) {
        return inventories.get(inventory);
    }

    public void clear() {
        inventories.clear();
    }
}
