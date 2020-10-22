package com.wizardlybump17.wlib.inventory;

import com.wizardlybump17.wlib.inventory.item.ItemButton;
import com.wizardlybump17.wlib.inventory.item.ItemClickAction;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;

public class CustomInventoryHolder implements InventoryHolder {

    private final Inventory inventory;
    private final Map<Integer, ItemButton> buttons = new HashMap<>();

    public CustomInventoryHolder(String title, int size) {
        inventory = Bukkit.createInventory(this, size, title);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void addButton(int slot, ItemButton button) {
        buttons.put(slot, button);
        inventory.setItem(slot, button.getItemStack());
    }

    public void removeButton(int slot) {
        inventory.clear(slot);
        buttons.remove(slot);
    }

    public void onClick(InventoryClickEvent event) {
        if (!buttons.containsKey(event.getRawSlot())) return;
        ItemClickAction itemClickAction = buttons.get(event.getRawSlot()).getItemClickAction();
        if (itemClickAction == null) return;
        itemClickAction.execute(event);
    }
}
