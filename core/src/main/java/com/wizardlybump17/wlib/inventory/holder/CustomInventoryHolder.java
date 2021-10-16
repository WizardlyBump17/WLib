package com.wizardlybump17.wlib.inventory.holder;

import com.wizardlybump17.wlib.inventory.CustomInventory;
import com.wizardlybump17.wlib.inventory.item.ItemButton;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CustomInventoryHolder {

    protected CustomInventory inventory;
    private final Map<Integer, ItemButton> buttons = new HashMap<>();
    private String shape;

    public CustomInventoryHolder(CustomInventory inventory) {
        this.inventory = inventory;
    }

    public void setInventory(CustomInventory inventory) {
        if (this.inventory == null)
            this.inventory = inventory;
    }

    public void setShape(String shape) {
        if (this.shape == null)
            this.shape = shape;
    }

    /**
     * Sets the item in the places where the char is equals the specified char
     * @param c the char
     * @param itemButton the item to be set
     */
    public void setButton(char c, ItemButton itemButton) {
        if (shape == null)
            return;

        final char[] chars = shape.toCharArray();
        for (int i = 0; i < chars.length; i++)
            if (chars[i] == c)
                setButton(i, itemButton);
    }

    public void setButton(int slot, ItemButton item) {
        inventory.getBukkitInventory().setItem(slot, item == null ? null : item.getItemStack());
        buttons.put(slot, item);
    }

    public void removeButton(int slot) {
        inventory.getBukkitInventory().setItem(slot, null);
        buttons.remove(slot);
    }

    public ItemButton getButton(int slot) {
        return buttons.get(slot);
    }

    public boolean hasButton(int slot) {
        return buttons.containsKey(slot);
    }

    public CustomInventory getOriginalInventory() {
        return inventory;
    }
}
