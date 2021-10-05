package com.wizardlybump17.wlib.inventory;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public interface InventoryEventListener<T extends Event> extends Listener {

    void listen(T event, CustomInventory inventory);
}
