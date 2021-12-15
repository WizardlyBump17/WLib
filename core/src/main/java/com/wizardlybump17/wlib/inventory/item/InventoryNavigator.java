package com.wizardlybump17.wlib.inventory.item;

import com.wizardlybump17.wlib.item.Item;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class InventoryNavigator {

    public static final InventoryNavigator NEXT_PAGE = new InventoryNavigator(
            Item.builder()
                    .type(Material.ARROW)
                    .displayName("§aNext Page")
                    .build(),
            '#'
    );
    public static final InventoryNavigator PREVIOUS_PAGE = new InventoryNavigator(
            Item.builder()
                    .type(Material.ARROW)
                    .displayName("§aPrevious Page")
                    .build(),
            '#'
    );

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
