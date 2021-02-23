package com.wizardlybump17.wlib.inventory.action;

import org.bukkit.event.inventory.InventoryEvent;

public interface InventoryAction<T extends InventoryEvent> {

    void execute(T event);
}
