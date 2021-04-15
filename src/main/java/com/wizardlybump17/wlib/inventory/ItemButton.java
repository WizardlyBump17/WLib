package com.wizardlybump17.wlib.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Data
public class ItemButton {

    private final ItemStack itemStack;
    private final ClickAction clickAction;

    public ItemButton(ItemStack item) {
        this(item, null);
    }

    public static interface ClickAction {
        void onClick(InventoryClickEvent event);
    }
}
