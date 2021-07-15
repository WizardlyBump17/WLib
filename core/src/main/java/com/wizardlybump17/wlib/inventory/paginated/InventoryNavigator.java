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

    public InventoryNavigator(ItemStack item, ItemButton replacer) {
        this.item = item;
        this.replacer = replacer;
    }

    public InventoryNavigator(ItemStack item, char replacer) {
        this.item = item;
        replacerChar = replacer;
    }
}
