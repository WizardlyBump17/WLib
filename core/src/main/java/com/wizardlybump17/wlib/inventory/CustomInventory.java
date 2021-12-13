package com.wizardlybump17.wlib.inventory;

import com.wizardlybump17.wlib.inventory.item.ItemButton;
import com.wizardlybump17.wlib.inventory.paginated.PaginatedInventory;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CustomInventory {

    private final Map<Integer, ItemButton> buttons;
    private final Inventory bukkitInventory;
    private PaginatedInventory paginatedHolder;

    public CustomInventory(String title, int size) {
        CustomInventoryHolder holder = new CustomInventoryHolder(this);
        bukkitInventory = Bukkit.createInventory(holder, size, title);
        holder.setInventory(bukkitInventory);
        buttons = new HashMap<>(size);
    }

    public void setPaginatedHolder(PaginatedInventory paginatedHolder) {
        if (this.paginatedHolder == null)
            this.paginatedHolder = paginatedHolder;
    }

    public void addButton(int slot, @NotNull ItemButton button) {
        buttons.put(slot, button);
        bukkitInventory.setItem(slot, button.getItem().get());
    }

    public void updateButton(int slot) {
        bukkitInventory.setItem(slot, buttons.get(slot).getItem().get());
    }

    public void removeButton(int slot) {
        buttons.remove(slot);
        bukkitInventory.clear(slot);
    }

    public boolean hasButton(int slot) {
        return buttons.containsKey(slot);
    }

    @Nullable
    public ItemButton getButton(int slot) {
        return buttons.get(slot);
    }
}
