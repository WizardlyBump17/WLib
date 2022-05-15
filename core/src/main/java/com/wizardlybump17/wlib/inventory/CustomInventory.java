package com.wizardlybump17.wlib.inventory;

import com.wizardlybump17.wlib.inventory.item.ItemButton;
import com.wizardlybump17.wlib.inventory.paginated.PaginatedInventory;
import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CustomInventory {

    private final Map<Integer, ItemButton> buttons;
    private Inventory bukkitInventory;
    private PaginatedInventory paginatedHolder;
    private final String shape;

    public CustomInventory(String shape, Inventory bukkitInventory) {
        this.bukkitInventory = bukkitInventory;
        buttons = new HashMap<>(shape.length());
        this.shape = shape;
    }

    public void setButtons() {
        setButtons(bukkitInventory);
    }

    public void setButtons(Inventory inventory) {
        inventory.clear();
        for (Map.Entry<Integer, ItemButton> entry : buttons.entrySet())
            inventory.setItem(entry.getKey(), entry.getValue().getItem().get());
        bukkitInventory = inventory;
    }

    public void setPaginatedHolder(PaginatedInventory paginatedHolder) {
        if (this.paginatedHolder == null)
            this.paginatedHolder = paginatedHolder;
    }

    public void addButton(int slot, @Nullable ItemButton button) {
        if (button == null)
            return;

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

    public int indexOf(char c) {
        return shape.indexOf(c);
    }

    public int getSize() {
        return bukkitInventory.getSize();
    }

    @Nullable
    public ItemButton getButton(int slot) {
        return buttons.get(slot);
    }

    public void updateButton(char button) {
        updateButton(indexOf(button));
    }

    public void updateButton(char button, ItemButton item) {
        int key = indexOf(button);
        if (key == -1)
            return;

        buttons.put(key, item);
        addButton(key, item);
    }
}
