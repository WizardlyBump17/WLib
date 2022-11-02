package com.wizardlybump17.wlib.util.bukkit;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang.Validate;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

@UtilityClass
public class InventoryUtil {

    /**
     * Checks if the give items can fit in the inventory
     * @param inventory the inventory to check against
     * @param items the items to check
     * @return if the items can fit in the inventory
     */
    public static boolean canFit(Inventory inventory, ItemStack... items) {
        Validate.noNullElements(items, "Item cannot be null");
        ItemStack[] inventoryItems = clone(inventory.getContents());
        items = clone(items);

        for (ItemStack item : items) {
            while (true) {
                int firstPartial = firstPartial(inventoryItems, item);
                if (firstPartial == -1) {
                    int firstFree = inventory.firstEmpty();
                    if (firstFree == -1)
                        return false;

                    if (item.getAmount() > inventory.getMaxStackSize()) {
                        ItemStack stack = item.clone();
                        stack.setAmount(inventory.getMaxStackSize());
                        setItem(inventoryItems, firstFree, stack);
                        item.setAmount(item.getAmount() - inventory.getMaxStackSize());
                    }

                    setItem(inventoryItems, firstFree, item);
                    break;
                }

                ItemStack partialItem = inventoryItems[firstPartial];
                int amount = item.getAmount();
                int partialAmount = partialItem.getAmount();
                int maxAmount = partialItem.getMaxStackSize();
                if (amount + partialAmount <= maxAmount) {
                    partialItem.setAmount(amount + partialAmount);
                    setItem(inventoryItems, firstPartial, partialItem);
                    break;
                }

                partialItem.setAmount(maxAmount);
                setItem(inventoryItems, firstPartial, partialItem);
                item.setAmount(amount + partialAmount - maxAmount);
            }
        }

        return true;
    }

    private static ItemStack[] clone(ItemStack... items) {
        ItemStack[] target = new ItemStack[items.length];
        for (int i = 0; i < items.length; i++)
            target[i] = items[i] == null ? null : items[i].clone();
        return target;
    }

    private static int firstPartial(ItemStack[] items, ItemStack item) {
        for (int i = 0; i < items.length; i++) {
            ItemStack cItem = items[i];
            if (cItem != null && cItem.getAmount() < cItem.getMaxStackSize() && cItem.isSimilar(item))
                return i;
        }

        return -1;
    }

    private static void setItem(ItemStack[] items, int slot, ItemStack item) {
        items[slot] = item;
    }

    /**
     * Checks if the given items can be removed from the item array
     * @param content the item array to check against
     * @param items the items to check
     * @return if the items can be removed from the item array
     */
    public static boolean canRemoveAll(ItemStack[] content, ItemStack... items) {
        Validate.notNull(items, "Items cannot be null");

        HashMap<Integer, ItemStack> leftover = new HashMap<>();
        content = clone(content);
        items = clone(items);

        for (int i = 0; i < items.length; ++i) {
            ItemStack item = items[i];
            int toDelete = item.getAmount();

            while (true) {
                int first = first(item, false, content);
                if (first == -1) {
                    item.setAmount(toDelete);
                    leftover.put(i, item);
                    break;
                }

                ItemStack itemStack = content[first];
                int amount = itemStack.getAmount();
                if (amount <= toDelete) {
                    toDelete -= amount;
                    content[first] = null;
                } else {
                    itemStack.setAmount(amount - toDelete);
                    setItem(content, first, itemStack);
                    toDelete = 0;
                }

                if (toDelete <= 0)
                    break;
            }
        }

        return leftover.isEmpty();
    }

    /**
     * Checks if the give items can be removed from the inventory
     * @param inventory the inventory to check against
     * @param items the items to check
     * @return if the items can be removed from the inventory
     */
    public static boolean canRemoveAll(Inventory inventory, ItemStack... items) {
        return canRemoveAll(inventory.getContents(), items);
    }

    private static int first(ItemStack item, boolean withAmount, ItemStack[] inventory) {
        if (item == null)
            return -1;

        int i = 0;

        while (true) {
            if (i >= inventory.length)
                return -1;

            if (inventory[i] != null) {
                if (withAmount) {
                    if (item.equals(inventory[i])) {
                        break;
                    }
                } else if (item.isSimilar(inventory[i])) {
                    break;
                }
            }

            i++;
        }

        return i;
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
}
