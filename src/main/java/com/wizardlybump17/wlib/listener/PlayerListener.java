package com.wizardlybump17.wlib.listener;

import com.wizardlybump17.wlib.inventory.CustomInventoryHolder;
import com.wizardlybump17.wlib.inventory.ItemButton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class PlayerListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (!(inventory.getHolder() instanceof CustomInventoryHolder)) return;
        event.setCancelled(true);

        CustomInventoryHolder holder = (CustomInventoryHolder) inventory.getHolder();
        if (!holder.hasButton(event.getRawSlot())) return;

        ItemButton item = holder.getButton(event.getRawSlot());
        if (item.getClickAction() != null)
            item.getClickAction().onClick(event);
    }
}
