package com.wizardlybump17.wlib.inventory;

import com.wizardlybump17.wlib.inventory.holder.CustomInventoryHolder;
import com.wizardlybump17.wlib.inventory.item.ItemButton;
import com.wizardlybump17.wlib.inventory.paginated.PaginatedInventory;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

@Data
public class CustomInventory {

    private final String title;
    private final int size;
    private final Inventory bukkitInventory;
    protected final CustomInventoryHolder owner;
    private PaginatedInventory paginatedHolder;

    public CustomInventory(String title, int size) {
        bukkitInventory = Bukkit.createInventory(owner = new CustomInventoryHolder(this), this.size = size, this.title = title);
    }

    public CustomInventory(String title, int size, int page) {
        bukkitInventory = Bukkit.createInventory(owner = new CustomInventoryHolder(this, page), this.size = size, this.title = title);
    }

    public CustomInventory(String title, int size, CustomInventoryHolder holder) {
        bukkitInventory = Bukkit.createInventory(owner = holder, this.size = size, this.title = title);
    }

    public void setPaginatedHolder(PaginatedInventory paginatedHolder) {
        if (this.paginatedHolder != null)
            return;
        this.paginatedHolder = paginatedHolder;
    }

    public void setButton(int slot, ItemButton button) {
        owner.setButton(slot, button);
    }

    public void setButton(char c, ItemButton button) {
        owner.setButton(c, button);
    }

    public void removeButton(int slot) {
        owner.removeButton(slot);
    }

    public ItemButton getButton(int slot) {
        return owner.getButton(slot);
    }

    public boolean hasButton(int slot) {
        return owner.hasButton(slot);
    }
}
