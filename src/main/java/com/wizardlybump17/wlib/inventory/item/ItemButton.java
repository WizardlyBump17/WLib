package com.wizardlybump17.wlib.inventory.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
public class ItemButton {

    private final ItemStack itemStack;
    private final ItemClickAction itemClickAction;

    public ItemButton(ItemStack itemStack) {
        this(itemStack, null);
    }
}
