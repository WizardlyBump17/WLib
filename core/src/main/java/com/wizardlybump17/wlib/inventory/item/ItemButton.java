package com.wizardlybump17.wlib.inventory.item;

import com.wizardlybump17.wlib.adapter.WMaterial;
import com.wizardlybump17.wlib.item.Item;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Data
public class ItemButton {

    public static final ItemButton AIR = new ItemButton(new ItemStack(Material.AIR));
    /**
     * Useful when creating inventory borders.
     * It returns a new ItemButton with no click action and with a black stained glass pane with the display name " "
     */
    public static final ItemButton BLACK_STAINED_GLASS_PANE = new ItemButton(Item.builder()
            .type(WMaterial.BLACK_STAINED_GLASS_PANE)
            .displayName(" ")
            .build());
    /**
     * Item for creating a back button
     */
    public static final ItemStack BACK_BARRIER = Item.builder()
            .type(Material.BARRIER)
            .displayName("§cBack")
            .build();

    private ItemStack itemStack;
    private final ClickAction clickAction;
    private final Map<Integer, ItemButton> children = new HashMap<>();

    public ItemButton(ItemStack item) {
        this(item, null);
    }

    public ItemButton(ItemStack item, ClickAction clickAction) {
        itemStack = item;
        this.clickAction = clickAction;
    }

    public void addChild(int slot, ItemButton item) {
        children.put(slot, item);
    }

    public void removeChild(int slot) {
        children.remove(slot);
    }
}
