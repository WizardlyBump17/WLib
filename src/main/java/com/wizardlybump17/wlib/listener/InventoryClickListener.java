package com.wizardlybump17.wlib.listener;

import com.wizardlybump17.wlib.inventory.CustomInventoryHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class InventoryClickListener extends WListener {

    public InventoryClickListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(event.getCurrentItem() == null || !(event.getInventory().getHolder() instanceof CustomInventoryHolder)) return;
        ((CustomInventoryHolder) event.getInventory().getHolder()).onClick(event);
        event.setCancelled(true);
    }
}
