package com.wizardlybump17.wlib.listener;

import com.wizardlybump17.wlib.WLib;
import com.wizardlybump17.wlib.inventory.CustomInventoryHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class InventoryCloseListener extends WListener<WLib> {

    public InventoryCloseListener(WLib plugin) {
        super(plugin);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if (!(inventory.getHolder() instanceof CustomInventoryHolder)) return;
        ((CustomInventoryHolder) inventory.getHolder()).onClose(event);
    }
}
