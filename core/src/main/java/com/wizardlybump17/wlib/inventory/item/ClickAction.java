package com.wizardlybump17.wlib.inventory.item;

import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Map;

public interface ClickAction {

    void onClick(InventoryClickEvent event, Map<String, Object> data);
}
