package com.wizardlybump17.wlib.inventory.paginated;

import com.wizardlybump17.wlib.inventory.CustomInventory;
import com.wizardlybump17.wlib.inventory.item.ItemButton;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class PaginatedInventoryBuilder {

    private final String title, shape;
    private final Map<Character, ItemButton> shapeReplacements = new HashMap<>();
    private ItemButton[] items;
    private ItemStack nextPageItemStack, previousPageItemStack;
    private char ifLastPage, ifFirstPage;

    public PaginatedInventoryBuilder(String title, String shape) {
        this.title = title;
        this.shape = shape;
    }

    public int getSize() {
        return shape.length();
    }

    @Deprecated
    public PaginatedInventoryBuilder border(ItemButton itemButton) {
        shapeReplacements.put('#', itemButton);
        return this;
    }

    public PaginatedInventoryBuilder items(ItemButton... itemButtons) {
        items = itemButtons;
        return this;
    }

    public PaginatedInventoryBuilder items(List<ItemButton> itemButtons) {
        return items(itemButtons.toArray(new ItemButton[]{}));
    }

    public PaginatedInventoryBuilder nextPageItemStack(ItemStack nextPageItemStack, char ifLast) {
        this.nextPageItemStack = nextPageItemStack;
        ifLastPage = ifLast;
        return this;
    }

    public PaginatedInventoryBuilder previousPageItemStack(ItemStack previousPageItemStack, char ifFirst) {
        this.previousPageItemStack = previousPageItemStack;
        ifFirstPage = ifFirst;
        return this;
    }

    public PaginatedInventoryBuilder addShapeReplacement(char character, ItemButton itemButton) {
        shapeReplacements.put(character, itemButton);
        return this;
    }

    public PaginatedInventoryBuilder removeShapeReplacement(char character) {
        shapeReplacements.remove(character);
        return this;
    }

    public PaginatedInventory build() {
        PaginatedInventory paginatedGui = new PaginatedInventory();

        char[] shapeChar = shape.toCharArray();
        int itemsSize = items == null ? 0 : items.length;
        int presetItems = 0;
        for (char c : shapeChar) if (c != 'x') presetItems++;

        if (previousPageItemStack != null)
            shapeReplacements.put('<', new ItemButton(previousPageItemStack, event -> paginatedGui.openPreviousPage((Player) event.getWhoClicked())));
        if (nextPageItemStack != null)
            shapeReplacements.put('>', new ItemButton(nextPageItemStack, event -> paginatedGui.openNextPage((Player) event.getWhoClicked())));

        int inventoriesSize;
        if (itemsSize < 1) inventoriesSize = 1;
        else inventoriesSize = (int) Math.ceil((double) itemsSize / (getSize() - presetItems));

        int currentItem = 0;
        for (int i = 0; i < inventoriesSize; i++) {
            CustomInventory customInventory = new CustomInventory(title, getSize());
            paginatedGui.addInventory(customInventory);

            for (int j = 0; j < getSize(); j++) {
                char currentChar = shapeChar[j];

                if (currentChar != 'x' && !shapeReplacements.containsKey(currentChar)) continue;

                if (currentChar == 'x' && itemsSize > 0) {
                    if (currentItem >= itemsSize) continue;
                    customInventory.item(j, items[currentItem]);
                    currentItem++;
                    continue;
                }

                if (currentChar == '<' && i == 0) {
                    customInventory.item(j, shapeReplacements.get(ifFirstPage));
                    continue;
                }

                if (currentChar == '>' && i + 1 == inventoriesSize) {
                    customInventory.item(j, shapeReplacements.get(ifLastPage));
                    continue;
                }

                customInventory.item(j, shapeReplacements.get(currentChar));
            }
        }
        return paginatedGui;
    }
}
