package com.wizardlybump17.wlib.inventory;

import com.wizardlybump17.wlib.inventory.item.ItemButton;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CustomInventoryHolder implements InventoryHolder {

    private final Inventory inventory;
    @Getter
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
        inventory.setItem(slot, button == null ? new ItemStack(Material.AIR) : button.getItemStack());
    }

    public void removeButton(int slot) {
        inventory.clear(slot);
        buttons.remove(slot);
    }

    public void onClick(InventoryClickEvent event) {
        if (!buttons.containsKey(event.getRawSlot())) return;
        ItemButton itemButton = buttons.get(event.getRawSlot());
        if (itemButton == null || itemButton.getItemClickAction() == null) return;
        itemButton.getItemClickAction().execute(event);
    }
}
