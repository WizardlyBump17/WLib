package com.wizardlybump17.wlib.listener;

import com.wizardlybump17.wlib.inventory.CustomInventoryHolder;
import com.wizardlybump17.wlib.inventory.ItemButton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onClick(InventoryClickEvent event) {
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null) return;

        Inventory clickedInventory = event.getClickedInventory();
        if (!(clickedInventory.getHolder() instanceof CustomInventoryHolder)) return;

        CustomInventoryHolder holder = (CustomInventoryHolder) clickedInventory.getHolder();
        if (!holder.hasButton(event.getRawSlot())) return;
        event.setCancelled(true);

        ItemButton item = holder.getButton(event.getRawSlot());
        if (item.getClickAction() != null)
            item.getClickAction().onClick(event);
    }
}
