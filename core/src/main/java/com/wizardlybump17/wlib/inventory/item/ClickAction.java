package com.wizardlybump17.wlib.inventory.item;

import com.wizardlybump17.wlib.inventory.paginated.PaginatedInventory;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface ClickAction {

    void onClick(InventoryClickEvent event, PaginatedInventory inventory);
}
