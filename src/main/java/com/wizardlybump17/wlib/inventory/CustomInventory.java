package com.wizardlybump17.wlib.inventory;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

@Data
public class CustomInventory {

    private final String title;
    private final int size;
    private final Inventory bukkitInventory;
    private final CustomInventoryHolder owner;

    public CustomInventory(String title, int size) {
        bukkitInventory = Bukkit.createInventory(owner = new CustomInventoryHolder(this), this.size = size, this.title = title);
    }
}
