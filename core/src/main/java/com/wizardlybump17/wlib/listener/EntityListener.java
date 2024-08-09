package com.wizardlybump17.wlib.listener;

import com.wizardlybump17.wlib.inventory.CustomInventory;
import com.wizardlybump17.wlib.inventory.CustomInventoryHolder;
import com.wizardlybump17.wlib.inventory.item.ClickAction;
import com.wizardlybump17.wlib.inventory.item.ItemButton;
import com.wizardlybump17.wlib.inventory.paginated.PaginatedInventory;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@RequiredArgsConstructor
public class EntityListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (!(inventory.getHolder() instanceof CustomInventoryHolder))
            return;

        PaginatedInventory paginatedInventory = ((CustomInventoryHolder) inventory.getHolder()).getHolder();

        event.setCancelled(true);

        final CustomInventory customInventory = paginatedInventory.getCurrentInventory();

        ItemButton item = customInventory.getButton(event.getRawSlot());
        if (item == null)
            return;

        ClickAction clickAction = item.getClickAction();
        if (clickAction != null)
            clickAction.onClick(event, paginatedInventory);
        item.getClickSounds().forEach(sound -> sound.play((Player) event.getWhoClicked()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDrag(InventoryDragEvent event) {
        Inventory inventory = event.getInventory();
        if (!(inventory.getHolder() instanceof CustomInventoryHolder))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        final InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof CustomInventoryHolder))
            return;

        final PaginatedInventory paginatedInventory = ((CustomInventoryHolder) holder).getHolder();
        if (!paginatedInventory.isUnregisterListeners())
            return;

        paginatedInventory.stopListeners();
    }
}
