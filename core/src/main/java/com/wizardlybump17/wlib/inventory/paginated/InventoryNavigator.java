package com.wizardlybump17.wlib.inventory.paginated;

import com.wizardlybump17.wlib.inventory.item.ItemButton;
import com.wizardlybump17.wlib.item.Item;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Data
@RequiredArgsConstructor
public class InventoryNavigator {

    /**
     * Common navigator that takes the viewer to the next page.
     * It uses an arrow with the display name "§aNext page".
     * Also, it uses the '#' char as replacer, but it can be bypassed using the constructors
     */
    public static InventoryNavigator NEXT_PAGE = new InventoryNavigator(
            Item.builder()
                    .type(Material.ARROW)
                    .displayName("§aNext page")
                    .build(),
            '#'
    );
    /**
     * Common navigator that takes the viewer to the previous page.
     * It uses an arrow with the display name "§aPrevious page".
     * Also, it uses the '#' char as replacer, but it can be bypassed using the constructors
     */
    public static InventoryNavigator PREVIOUS_PAGE = new InventoryNavigator(
            Item.builder()
                    .type(Material.ARROW)
                    .displayName("§aPrevious page")
                    .build(),
            '#'
    );

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
