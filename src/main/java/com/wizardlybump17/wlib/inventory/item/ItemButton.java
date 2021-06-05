package com.wizardlybump17.wlib.inventory.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Data
public class ItemButton {

    public static final ItemButton AIR = new ItemButton(new ItemStack(Material.AIR));

    private ItemStack itemStack;
    private final ClickAction clickAction;

    public ItemButton(ItemStack item) {
        this(item, null);
    }

    public static interface ClickAction {
        void onClick(InventoryClickEvent event);
    }
}
