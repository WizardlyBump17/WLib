package com.wizardlybump17.wlib.inventory;

import com.wizardlybump17.wlib.inventory.item.ItemButton;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CustomInventoryHolder implements InventoryHolder {

    private final Inventory inventory;
    private final Map<Integer, ItemButton> buttons = new HashMap<>();
    private final CloseInventoryAction closeAction;
    @Setter private boolean ignoreCloseEvent;

    public CustomInventoryHolder(String title, int size) {
        this(title, size, null);
    }

    public CustomInventoryHolder(String title, int size, CloseInventoryAction closeAction) {
        inventory = Bukkit.createInventory(this, size, title);
        this.closeAction = closeAction;
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

    public void onClose(InventoryCloseEvent event) {
        if (closeAction != null && !ignoreCloseEvent) closeAction.onClose(event);
    }
}
