package com.wizardlybump17.wlib.inventory.paginated;

import com.wizardlybump17.wlib.inventory.CustomInventory;
import com.wizardlybump17.wlib.inventory.CustomInventoryHolder;
import com.wizardlybump17.wlib.inventory.item.InventoryNavigator;
import com.wizardlybump17.wlib.inventory.item.ItemButton;
import com.wizardlybump17.wlib.inventory.listener.InventoryListener;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.UnaryOperator;

public class PaginatedInventoryBuilder {

    private String title;
    private String shape;
    private final Map<Character, ItemButton> shapeReplacements = new HashMap<>();
    private List<ItemButton> content = new ArrayList<>();
    private InventoryNavigator nextPage;
    private InventoryNavigator previousPage;
    private List<InventoryListener<?>> listeners = new ArrayList<>();
    private Map<String, Object> initialData;

    private PaginatedInventoryBuilder() {
    }

    public static PaginatedInventoryBuilder create() {
        return new PaginatedInventoryBuilder();
    }

    public PaginatedInventoryBuilder mapContent(UnaryOperator<ItemButton> function) {
        for (int i = 0; i < content.size(); i++)
            content.set(i, function.apply(content.get(i)));
        return this;
    }

    public PaginatedInventoryBuilder listeners(List<InventoryListener<?>> listeners) {
        this.listeners = listeners == null ? new ArrayList<>() : listeners;
        return this;
    }

    public PaginatedInventoryBuilder title(String title) {
        this.title = title;
        return this;
    }

    public PaginatedInventoryBuilder shape(String shape) {
        this.shape = shape;
        return this;
    }

    public PaginatedInventoryBuilder nextPage(ItemStack item) {
        return nextPage(new InventoryNavigator(item));
    }

    public PaginatedInventoryBuilder previousPage(ItemStack item) {
        return previousPage(new InventoryNavigator(item));
    }

    public PaginatedInventoryBuilder nextPage(InventoryNavigator nextPage) {
        this.nextPage = nextPage;
        return this;
    }

    public PaginatedInventoryBuilder previousPage(InventoryNavigator previousPage) {
        this.previousPage = previousPage;
        return this;
    }

    public PaginatedInventoryBuilder shapeReplacement(char character, ItemButton itemButton) {
        shapeReplacements.put(character, itemButton);
        return this;
    }

    public PaginatedInventoryBuilder content(List<ItemButton> content) {
        this.content = content == null ? new ArrayList<>() : content;
        return this;
    }

    public PaginatedInventoryBuilder content(ItemButton... content) {
        this.content = new ArrayList<>(Arrays.asList(content));
        return this;
    }

    public PaginatedInventoryBuilder listener(InventoryListener<?> listener) {
        listeners.add(listener);
        return this;
    }

    public PaginatedInventoryBuilder initialData(Map<String, Object> initialData) {
        this.initialData = initialData;
        return this;
    }

    public PaginatedInventoryBuilder data(String key, Object value) {
        if (initialData == null)
            initialData = new HashMap<>();
        initialData.put(key, value);
        return this;
    }

    public PaginatedInventory build() {
        if (title == null)
            throw new IllegalArgumentException("Title cannot be null");
        if (shape == null)
            throw new IllegalArgumentException("Shape cannot be null");

        int pages = getPages();
        ArrayList<CustomInventory> inventories = new ArrayList<>(pages);
        PaginatedInventory paginatedInventory = new PaginatedInventory(inventories, listeners, initialData == null ? new HashMap<>() : initialData);

        char[] shapeChars = shape.toCharArray();

        CustomInventoryHolder holder = new CustomInventoryHolder(paginatedInventory);
        Inventory bukkitInventory = Bukkit.createInventory(holder, shape.length(), title);

        int currentItem = 0;
        for (int page = 0; page < pages; page++) {
            CustomInventory inventory = new CustomInventory(shape, bukkitInventory);
            inventory.setPaginatedHolder(paginatedInventory);
            inventories.add(inventory);

            for (int slot = 0; slot < shape.length(); slot++) {
                char c = shapeChars[slot];

                switch (c) {
                    case 'x': {
                        if (content.isEmpty() || currentItem >= content.size())
                            continue;

                        ItemButton item = content.get(currentItem++);
                        if (item != null)
                            inventory.addButton(slot, item);
                        continue;
                    }

                    case '>':
                        setNavigator(slot, inventory, true, page, pages);
                        continue;

                    case '<':
                        setNavigator(slot, inventory, false, page, pages);
                        continue;

                    default: {
                        if (shapeReplacements.containsKey(c))
                            inventory.addButton(slot, shapeReplacements.get(c));
                    }
                }
            }
        }

        return paginatedInventory;
    }

    private void setNavigator(int slot, CustomInventory inventory, boolean next, int page, int maxPages) {
        if (!next) {
            if (page == 0) {
                if (previousPage.getReplacementChar() != null) {
                    inventory.addButton(slot, shapeReplacements.get(previousPage.getReplacementChar()));
                    return;
                }

                if (previousPage.getReplacementButton() != null)
                    inventory.addButton(slot, previousPage.getReplacementButton());
                return;
            }
            inventory.addButton(slot, new ItemButton(previousPage.getItem(), (event, inventory1) -> inventory.getPaginatedHolder().showPreviousPage(event.getWhoClicked())));
            return;
        }

        if (page == maxPages - 1) {
            if (nextPage.getReplacementChar() != null) {
                inventory.addButton(slot, shapeReplacements.get(nextPage.getReplacementChar()));
                return;
            }

            if (nextPage.getReplacementButton() != null)
                inventory.addButton(slot, nextPage.getReplacementButton());
            return;
        }
        inventory.addButton(slot, new ItemButton(nextPage.getItem(), (event, inventory1) -> inventory.getPaginatedHolder().showNextPage(event.getWhoClicked())));
    }

    private int getPages() {
        int presetItems = 0;
        for (char c : shape.toCharArray())
            if (c != 'x')
                presetItems++;

        return content.isEmpty() ? 1 : (int) Math.ceil((double) content.size() / (shape.length() - presetItems));
    }
}
