package com.wizardlybump17.wlib.inventory.item;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class InventoryNavigator {

    private final ItemStack item;
    private final Character replacementChar;
    private final ItemButton replacementButton;

    public InventoryNavigator(ItemStack item) {
        this(item, null, null);
    }

    public InventoryNavigator(ItemStack item, Character replacementChar) {
        this(item, replacementChar, null);
    }

    public InventoryNavigator(ItemStack item, ItemButton replacementButton) {
        this(item, null, replacementButton);
    }
}
