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
    private final int size;
    private final Map<Character, ItemButton> shapeReplacements = new HashMap<>();
    private ItemButton border;
    private ItemButton[] content;
    private ItemStack nextPageItemStack, previousPageItemStack;

    public PaginatedInventoryBuilder(String title, int size, String shape) {
        this.title = title;
        this.size = size;
        this.shape = shape;
    }

    public PaginatedInventoryBuilder border(ItemButton itemButton) {
        shapeReplacements.put('#', border = itemButton);
        return this;
    }

    public PaginatedInventoryBuilder items(ItemButton... itemButtons) {
        content = itemButtons;
        return this;
    }

    public PaginatedInventoryBuilder items(List<ItemButton> itemButtons) {
        return items(itemButtons.toArray(new ItemButton[]{}));
    }

    public PaginatedInventoryBuilder nextPageItemStack(ItemStack nextPageItemStack) {
        this.nextPageItemStack = nextPageItemStack;
        return this;
    }

    public PaginatedInventoryBuilder previousPageItemStack(ItemStack previousPageItemStack) {
        this.previousPageItemStack = previousPageItemStack;
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

        int contentSize = content.length;
        int borderSize = border == null ? 0 : size == 9 ? 9 : (size / 9 - 2) * 2 + 18;

        int presetItems = 0;
        for (char c : shapeChar) if (shapeReplacements.containsKey(c) && c != '#') presetItems++;

        int inventoriesSize = contentSize / (size - borderSize - presetItems);
        if (contentSize % (size - borderSize - presetItems) != 0) inventoriesSize++;

        System.out.println(inventoriesSize);
        System.out.println(presetItems);

        shapeReplacements.put('>',
                new ItemButton(
                        nextPageItemStack,
                        event -> paginatedGui.openNextPage((Player) event.getWhoClicked())));
        shapeReplacements.put('<',
                new ItemButton(
                        previousPageItemStack,
                        event -> paginatedGui.openPreviousPage((Player) event.getWhoClicked())));

        int currentItem = 0;
        for (int i = 0; i < inventoriesSize; i++) {
            CustomInventory customInventory = new CustomInventory(title, size);
            paginatedGui.addInventory(customInventory);

            for (int j = 0; j < shapeChar.length; j++) {
                char currentChar = shapeChar[j];
                if (!shapeReplacements.containsKey(currentChar)) continue;
                customInventory.item(j, shapeReplacements.getOrDefault(currentChar, new ItemButton(new ItemStack(Material.AIR))));
            }
            for (int j = 0; j < size; j++) {
                char currentChar = shapeChar[j];
                if (shapeReplacements.containsKey(currentChar)) continue;
                if (customInventory.isFull()) break;
                customInventory.item(j, content[currentItem]);
                if (currentItem + 1 >= contentSize) break;
                currentItem++;
            }
        }
        return paginatedGui;
    }
}
