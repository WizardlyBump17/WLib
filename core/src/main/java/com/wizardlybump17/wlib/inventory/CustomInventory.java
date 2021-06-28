package com.wizardlybump17.wlib.inventory;

import com.wizardlybump17.wlib.inventory.holder.CustomInventoryHolder;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

@Data
public class CustomInventory {

    private final String title;
    private final int size;
    private final Inventory bukkitInventory;
    protected final CustomInventoryHolder owner;

    public CustomInventory(String title, int size) {
        bukkitInventory = Bukkit.createInventory(owner = new CustomInventoryHolder(this), this.size = size, this.title = title);
    }

    public CustomInventory(String title, int size, CustomInventoryHolder holder) {
        bukkitInventory = Bukkit.createInventory(owner = holder, this.size = size, this.title = title);
    }
}
