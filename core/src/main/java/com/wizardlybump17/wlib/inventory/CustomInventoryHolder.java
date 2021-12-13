package com.wizardlybump17.wlib.inventory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@Getter
@RequiredArgsConstructor
public class CustomInventoryHolder implements InventoryHolder {

    private final CustomInventory customInventory;
    private Inventory inventory;

    protected void setInventory(Inventory inventory) {
        if (this.inventory == null)
            this.inventory = inventory;
    }
}
