package com.wizardlybump17.wlib.inventory.paginated;

import com.google.gson.Gson;
import com.wizardlybump17.wlib.inventory.CustomInventory;
import com.wizardlybump17.wlib.inventory.UpdatableInventory;
import com.wizardlybump17.wlib.inventory.holder.CustomInventoryHolder;
import com.wizardlybump17.wlib.inventory.holder.UpdatableHolder;
import com.wizardlybump17.wlib.inventory.item.ItemButton;
import com.wizardlybump17.wlib.inventory.item.UpdatableItem;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class PaginatedInventoryBuilder implements Cloneable {

    private static final Gson GSON = new Gson();

    private List<ItemButton> content;
    private String title, shape;
    private final Map<Character, ItemButton> shapeReplacements = new HashMap<>();
    private final Map<Character, UpdatableItem> updatableShapeReplacements = new HashMap<>();
    private InventoryNavigator nextPage, previousPage;
    private int updateTime;

    public PaginatedInventoryBuilder updateTime(int time) {
        updateTime = time;
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

    public PaginatedInventoryBuilder item(ItemButton item) {
        if (content == null)
            content = new ArrayList<>();
        content.add(item);
        return this;
    }

    public PaginatedInventoryBuilder content(List<ItemButton> content) {
        this.content = content;
        return this;
    }

    public PaginatedInventoryBuilder shapeReplacement(char character, ItemButton item) {
        if (item instanceof UpdatableItem)
            updatableShapeReplacements.put(character, (UpdatableItem) item);
        else
            shapeReplacements.put(character, item);
        return this;
    }

    public PaginatedInventoryBuilder nextPage(InventoryNavigator navigator) {
        nextPage = navigator;
        return this;
    }

    public PaginatedInventoryBuilder previousPage(InventoryNavigator navigator) {
        previousPage = navigator;
        return this;
    }

    public PaginatedInventory build() {
        if (title == null || shape == null)
            throw new NullPointerException("title or shape is null");
        PaginatedInventory paginatedInventory = new PaginatedInventory();

        char[] shapeChar = shape.toCharArray();
        int presetItems = 0;
        for (char c : shapeChar)
            if (c != 'x')
                presetItems++;

        int contentSize = 0;
        if (content != null)
            for (ItemButton button : content)
                if (button != null) contentSize++;
        int inventoriesAmount = contentSize == 0
                ? 1
                : (int) Math.ceil((double) contentSize / (shape.length() - presetItems));

        int currentItem = 0;
        for (int i = 0; i < inventoriesAmount; i++) {
            CustomInventory inventory = new CustomInventory(title.replace("{page}", Integer.toString(i + 1)), shape.length());
            CustomInventoryHolder holder = inventory.getOwner();

            for (int slot = 0; slot < shape.length(); slot++) {
                char currentChar = shapeChar[slot];

                switch (currentChar) {
                    case 'x': {
                        if (contentSize > 0 && currentItem < content.size()) {
                            ItemButton button = content.get(currentItem++);
                            if (button instanceof UpdatableItem) {
                                if (!(holder instanceof UpdatableHolder)) {
                                    inventory = fromHolder(i, holder);
                                    holder = inventory.getOwner();
                                }
                            }
                            if (button == null) continue;
                            holder.setButton(slot, button);
                        }
                        continue;
                    }

                    case '<': {
                        setNavigator(i, inventoriesAmount, slot, holder, previousPage, paginatedInventory, false);
                        continue;
                    }

                    case '>':
                        setNavigator(i, inventoriesAmount, slot, holder, nextPage, paginatedInventory, true);
                        continue;

                    default: {
                        if (shapeReplacements.containsKey(currentChar)) {
                            holder.setButton(slot, shapeReplacements.get(currentChar));
                            continue;
                        }
                        if (updatableShapeReplacements.containsKey(currentChar)) {
                            if (holder instanceof UpdatableHolder) {
                                holder.setButton(slot, updatableShapeReplacements.get(currentChar));
                                continue;
                            }

                            inventory = fromHolder(i, holder);
                            (holder = inventory.getOwner()).setButton(slot, updatableShapeReplacements.get(currentChar));
                        }
                    }
                }
            }

            paginatedInventory.addInventory(inventory);
        }

        return paginatedInventory;
    }

    private UpdatableInventory fromHolder(int page, CustomInventoryHolder original) {
        UpdatableInventory inventory = new UpdatableInventory(title.replace("{page}", Integer.toString(page + 1)), shape.length(), updateTime);
        UpdatableHolder tempHolder = (UpdatableHolder) inventory.getOwner();
        for (Map.Entry<Integer, ItemButton> entry : original.getButtons().entrySet())
            tempHolder.setButton(entry.getKey(), entry.getValue());
        return inventory;
    }

    private void setNavigator(int page, int totalInventories, int slot, CustomInventoryHolder holder, InventoryNavigator navigator, PaginatedInventory inventories, boolean next) {
        ItemButton item;
        if (navigator.replacer != null)
            item = navigator.replacer;
        else if (navigator.replacerChar != '\u0000')
            item = shapeReplacements.get(navigator.replacerChar);
        else
            item = new ItemButton(navigator.item);

        if (next) {
            if (page + 1 < totalInventories)
                holder.setButton(slot, new ItemButton(
                        navigator.item,
                        event -> inventories.showNextPage(event.getWhoClicked())
                ));
            else
                holder.setButton(slot, item);
            return;
        }

        if (page != 0)
            holder.setButton(slot, new ItemButton(
                    navigator.item,
                    event -> inventories.showPreviousPage(event.getWhoClicked())
            ));
        else
            holder.setButton(slot, item);
    }

    @Override
    public PaginatedInventoryBuilder clone() {
        try {
            PaginatedInventoryBuilder builder = (PaginatedInventoryBuilder) super.clone();
            builder.content = new ArrayList<>();
            return builder;
        } catch (Exception e) {
            return null;
        }
    }
}
