package com.wizardlybump17.wlib.inventory.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Data
public class ItemButton {

    public static final ItemButton AIR = new ItemButton(new ItemStack(Material.AIR));

    private ItemStack itemStack;
    private final ClickAction clickAction;
    private final Map<Integer, ItemButton> children = new HashMap<>();

    public ItemButton(ItemStack item) {
        this(item, null);
    }

    public void addChild(int slot, ItemButton item) {
        children.put(slot, item);
    }

    public void removeChild(int slot) {
        children.remove(slot);
    }

    public interface ClickAction {
        void onClick(InventoryClickEvent event);
    }
}
