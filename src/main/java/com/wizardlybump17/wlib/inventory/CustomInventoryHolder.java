package com.wizardlybump17.wlib.inventory;

import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class CustomInventoryHolder implements InventoryHolder {

    private final CustomInventory inventory;
    private final Map<Integer, ItemButton> buttons = new HashMap<>();

    public void setButton(int slot, ItemButton item) {
        getInventory().setItem(slot, item.getItemStack());
        buttons.put(slot, item);
    }

    public void removeButton(int slot) {
        getInventory().setItem(slot, null);
        buttons.remove(slot);
    }

    public ItemButton getButton(int slot) {
        return buttons.get(slot);
    }

    public boolean hasButton(int slot) {
        return buttons.containsKey(slot);
    }

    @Override
    public Inventory getInventory() {
        return inventory.getBukkitInventory();
    }
}
