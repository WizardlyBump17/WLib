package com.wizardlybump17.wlib.util.bukkit;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class InventoryUtil {

    /**
     * Checks if the give items can fit in the inventory
     * @param inventory the inventory to check against
     * @param items the items to check
     * @return if the items can fit in the inventory
     */
    public static boolean canFit(@NonNull Inventory inventory, ItemStack @NonNull ... items) {
        Inventory inventoryCopy;
        if (inventory.getType() == InventoryType.PLAYER)
            inventoryCopy = Bukkit.createInventory(null, 36);
        else
            inventoryCopy = Bukkit.createInventory(null, inventory.getType());

        inventoryCopy.setContents(clone(inventory.getStorageContents()));
        return inventoryCopy.addItem(clone(items)).isEmpty();
    }

    /**
     * Checks if the give items can be removed from the inventory
     * @param inventory the inventory to check against
     * @param items the items to check
     * @return if the items can be removed from the inventory
     */
    public static boolean canRemoveAll(Inventory inventory, ItemStack... items) {
        Inventory clone = Bukkit.createInventory(null, inventory.getType());
        clone.setContents(clone(inventory.getContents()));
        return clone.removeItem(clone(items)).isEmpty();
    }

    /**
     * Gets the enough space for the given item in the inventory
     * @param inventory the inventory to check against
     * @param item the item to check
     * @return the enough space for the given item in the inventory
     */
    public static int getEnoughSpace(Inventory inventory, ItemStack item) {
        int space = 0;
        for (ItemStack i : inventory) {
            if (i == null)
                space += item.getMaxStackSize();
            else if (i.isSimilar(item))
                space += i.getMaxStackSize() - i.getAmount();
        }
        return space;
    }

    /**
     * Removes the given items from the inventory.<br>
     * It will check only the material type and amount
     * @param inventory the inventory to remove the items from
     * @param items the items to remove
     * @return a {@link Map} with the items that couldn't be removed
     */
    public static Map<Integer, ItemStack> removeMaterialAmount(Inventory inventory, ItemStack @NonNull ... items) {
        Map<Integer, ItemStack> leftover = new HashMap<>();

        for (int i = 0; i < items.length; ++i) {
            ItemStack item = items[i];
            int toDelete = item.getAmount();

            while (true) {
                int first = inventory.first(item.getType());
                if (first == -1) {
                    item.setAmount(toDelete);
                    leftover.put(i, item);
                    break;
                }

                ItemStack itemStack = inventory.getItem(first);
                int amount = itemStack.getAmount();
                if (amount <= toDelete) {
                    toDelete -= amount;
                    inventory.clear(first);
                } else {
                    itemStack.setAmount(amount - toDelete);
                    inventory.setItem(first, itemStack);
                    toDelete = 0;
                }

                if (toDelete <= 0)
                    break;
            }
        }

        return leftover;
    }

    public static ItemStack[] clone(ItemStack[] original) {
        ItemStack[] clone = new ItemStack[original.length];
        for (int i = 0; i < original.length; i++)
            clone[i] = original[i] == null ? null : original[i].clone();
        return clone;
    }
}
