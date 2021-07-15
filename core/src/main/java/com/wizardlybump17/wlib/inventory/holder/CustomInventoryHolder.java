package com.wizardlybump17.wlib.inventory.holder;

import com.wizardlybump17.wlib.inventory.CustomInventory;
import com.wizardlybump17.wlib.inventory.item.ItemButton;
import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;

public class CustomInventoryHolder implements InventoryHolder {

    protected CustomInventory inventory;
    @Getter
    private final Map<Integer, ItemButton> buttons = new HashMap<>();

    public CustomInventoryHolder(CustomInventory inventory) {
        this.inventory = inventory;
    }

    public void setInventory(CustomInventory inventory) {
        if (this.inventory == null)
            this.inventory = inventory;
    }

    public void setButton(int slot, ItemButton item) {
        getInventory().setItem(slot, item == null ? null : item.getItemStack());
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

    public CustomInventory getOriginalInventory() {
        return inventory;
    }
}
