package com.wizardlybump17.wlib.inventory;

import org.bukkit.event.inventory.InventoryCloseEvent;

public interface CloseInventoryAction {

    void onClose(InventoryCloseEvent event);
}
