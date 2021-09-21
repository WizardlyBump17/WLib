package com.wizardlybump17.wlib.inventory.paginated;

import com.wizardlybump17.wlib.inventory.item.ItemButton;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

@Data
@RequiredArgsConstructor
public class InventoryNavigator {

    protected final ItemStack item;
    protected ItemButton replacer;
    protected char replacerChar;

    /**
     * Creates a new navigator using the other navigator item
     * @param other the other navigator
     */
    public InventoryNavigator(InventoryNavigator other) {
        this.item = other.item;
    }

    /**
     * Creates a new navigator using the other navigator item but with the specified {@link ItemButton} replacer
     * @param other the other navigator
     * @param replacer the replacer
     */
    public InventoryNavigator(InventoryNavigator other, ItemButton replacer) {
        this.item = other.item;
        this.replacer = replacer;
    }

    /**
     * Creates a new navigator using the other navigator item but with the specified char replacer
     * @param other the other navigator
     * @param replacer the replacer
     */
    public InventoryNavigator(InventoryNavigator other, char replacer) {
        this.item = other.item;
        replacerChar = replacer;
    }

    public InventoryNavigator(ItemStack item, ItemButton replacer) {
        this.item = item;
        this.replacer = replacer;
    }

    public InventoryNavigator(ItemStack item, char replacer) {
        this.item = item;
        replacerChar = replacer;
    }
}
