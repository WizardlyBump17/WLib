package com.wizardlybump17.wlib.listener;

import com.wizardlybump17.wlib.WLib;
import com.wizardlybump17.wlib.inventory.CustomInventoryHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener extends WListener<WLib> {

    public InventoryClickListener(WLib plugin) {
        super(plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(event.getCurrentItem() == null || !(event.getInventory().getHolder() instanceof CustomInventoryHolder)) return;
        ((CustomInventoryHolder) event.getInventory().getHolder()).onClick(event);
        event.setCancelled(true);
    }
}
