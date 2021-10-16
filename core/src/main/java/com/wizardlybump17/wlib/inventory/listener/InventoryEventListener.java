package com.wizardlybump17.wlib.inventory.listener;

import com.wizardlybump17.wlib.inventory.CustomInventory;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public interface InventoryEventListener<T extends Event> extends Listener {

    void listen(T event, CustomInventory inventory);
}
