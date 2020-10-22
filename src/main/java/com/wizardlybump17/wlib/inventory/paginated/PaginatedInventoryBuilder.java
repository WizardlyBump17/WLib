package com.wizardlybump17.wlib.inventory.paginated;

import com.wizardlybump17.wlib.inventory.CustomInventory;
import com.wizardlybump17.wlib.inventory.item.ItemButton;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class PaginatedInventoryBuilder {

    private final String title;
    private final int size;
    private final String shape;
    private ItemButton border;
    private ItemButton[] content;
    private ItemStack nextPageItemStack, previousPageItemStack;

    public PaginatedInventoryBuilder(String title, int size, String shape) {
        this.title = title;
        this.size = size;
        this.shape = shape;
    }

    public PaginatedInventoryBuilder border(ItemButton itemButton) {
        border = itemButton;
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

    public PaginatedInventory build() {
        PaginatedInventory paginatedGui = new PaginatedInventory();
        int contentSize = content.length;
        int borderSize = border == null ? 0 : size == 9 ? 9 : (size / 9 - 2) * 2 + 18;
        int inventoriesSize = contentSize / (size - borderSize);
        if (contentSize % (size - borderSize) != 0) inventoriesSize++;
        int currentItem = 0;
        for (int i = 0; i < inventoriesSize; i++) {
            CustomInventory customInventory = new CustomInventory(title, size);
            paginatedGui.addInventory(customInventory);
            char[] charArray = shape.toCharArray();
            for (int j = 0; j < charArray.length; j++) {
                char currentChar = charArray[j];
                if (currentChar == '#') customInventory.item(j, border);
                if (currentChar == '>') customInventory.item(j,
                        new ItemButton(nextPageItemStack,
                                event -> {
                                    paginatedGui.nextPage((Player) event.getWhoClicked());
                                }));
                if (currentChar == '<') customInventory.item(j,
                        new ItemButton(previousPageItemStack,
                                event -> {
                                    paginatedGui.previousPage((Player) event.getWhoClicked());
                                }));
            }
            for (int j = 0; j < size; j++) {
                if (customInventory.isEmpty()) break;
                customInventory.item(content[currentItem]);
                if (currentItem + 1 >= contentSize) break;
                currentItem++;
            }
        }
        return paginatedGui;
    }
}
