package com.wizardlybump17.wlib.inventory.listener;

import com.wizardlybump17.wlib.inventory.paginated.PaginatedInventory;
import org.bukkit.event.Event;

public interface ListenerConsumer<T extends Event> {

    void fire(T event, PaginatedInventory inventory);
}
