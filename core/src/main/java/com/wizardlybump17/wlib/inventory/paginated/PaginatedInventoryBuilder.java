package com.wizardlybump17.wlib.inventory.paginated;

import com.wizardlybump17.wlib.inventory.CustomInventory;
import com.wizardlybump17.wlib.inventory.CustomInventoryHolder;
import com.wizardlybump17.wlib.inventory.item.InventoryNavigator;
import com.wizardlybump17.wlib.inventory.item.ItemButton;
import com.wizardlybump17.wlib.inventory.listener.InventoryListener;
import com.wizardlybump17.wlib.util.MapUtils;
import com.wizardlybump17.wlib.util.ObjectUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@SerializableAs("paginated-inventory-builder")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Accessors(fluent = true)
public class PaginatedInventoryBuilder implements ConfigurationSerializable, Cloneable {

    private String title;
    private String shape;
    @NotNull
    private Map<Character, ItemButton> shapeReplacements = new HashMap<>();
    private List<ItemButton> content = new ArrayList<>();
    @Nullable
    private InventoryNavigator nextPage;
    @Nullable
    private InventoryNavigator previousPage;
    @NotNull
    private List<InventoryListener<?>> listeners = new ArrayList<>();
    @NotNull
    private Map<Object, Object> initialData = new HashMap<>();

    private PaginatedInventoryBuilder() {
    }

    public static PaginatedInventoryBuilder create() {
        return new PaginatedInventoryBuilder();
    }

    public PaginatedInventoryBuilder mapContent(@NonNull UnaryOperator<ItemButton> function) {
        content.replaceAll(function);
        return this;
    }

    public PaginatedInventoryBuilder listeners(@Nullable List<InventoryListener<?>> listeners) {
        this.listeners = listeners == null ? new ArrayList<>() : listeners;
        return this;
    }

    public PaginatedInventoryBuilder title(@NonNull String title) {
        this.title = title;
        return this;
    }

    public PaginatedInventoryBuilder shape(@NonNull String shape) {
        this.shape = shape;
        return this;
    }

    public PaginatedInventoryBuilder nextPage(@NonNull ItemStack item) {
        return nextPage(new InventoryNavigator(item));
    }

    public PaginatedInventoryBuilder previousPage(@NonNull ItemStack item) {
        return previousPage(new InventoryNavigator(item));
    }

    public PaginatedInventoryBuilder nextPage(@Nullable InventoryNavigator nextPage) {
        this.nextPage = nextPage;
        return this;
    }

    public PaginatedInventoryBuilder previousPage(@Nullable InventoryNavigator previousPage) {
        this.previousPage = previousPage;
        return this;
    }

    public PaginatedInventoryBuilder shapeReplacement(char character, @NonNull ItemButton itemButton) {
        shapeReplacements.put(character, itemButton);
        return this;
    }

    public PaginatedInventoryBuilder shapeReplacement(char character, @NonNull Supplier<ItemButton> supplier) {
        return shapeReplacement(character, supplier.get());
    }

    public PaginatedInventoryBuilder shapeReplacements(@NonNull Map<Character, ItemButton> shapeReplacements) {
        this.shapeReplacements.clear();
        this.shapeReplacements.putAll(shapeReplacements);
        return this;
    }

    public PaginatedInventoryBuilder content(@Nullable List<ItemButton> content) {
        this.content = content == null ? new ArrayList<>() : content;
        checkNullContent();
        return this;
    }

    public PaginatedInventoryBuilder content(@NonNull ItemButton... content) {
        this.content = Arrays.asList(content);
        checkNullContent();
        return this;
    }

    private void checkNullContent() {
        for (ItemButton button : content)
            if (button == null)
                throw new NullPointerException("Content cannot contain null values.");
    }

    public PaginatedInventoryBuilder listener(@NonNull InventoryListener<?> listener) {
        listeners.add(listener);
        return this;
    }

    public PaginatedInventoryBuilder initialData(@Nullable Map<Object, Object> initialData) {
        this.initialData = initialData == null ? new HashMap<>() : initialData;
        return this;
    }

    public PaginatedInventoryBuilder data(@NonNull Object key, @Nullable Object value) {
        initialData.put(key, value);
        return this;
    }

    public PaginatedInventory build() {
        Objects.requireNonNull(title, "Title cannot be null.");
        Objects.requireNonNull(shape, "Shape cannot be null.");

        int pages = getPages();
        ArrayList<CustomInventory> inventories = new ArrayList<>(pages);
        PaginatedInventory paginatedInventory = new PaginatedInventory(inventories, listeners, initialData);

        char[] shapeChars = shape.toCharArray();

        CustomInventoryHolder holder = new CustomInventoryHolder(paginatedInventory);
        Inventory bukkitInventory = Bukkit.createInventory(holder, shape.length(), title);

        int currentItem = 0;
        for (int page = 0; page < pages; page++) {
            CustomInventory inventory = new CustomInventory(shape, bukkitInventory);
            inventory.setPaginatedHolder(paginatedInventory);
            inventories.add(inventory);

            int slot = 0;
            for (char c : shapeChars) {
                switch (c) {
                    case 'x' -> {
                        if (content.isEmpty() || currentItem >= content.size()) {
                            slot++;
                            continue;
                        }

                        inventory.addButton(slot++, content.get(currentItem++));
                    }
                    case '>' -> setNavigator(slot++, inventory, true, page, pages);
                    case '<' -> setNavigator(slot++, inventory, false, page, pages);
                    default -> inventory.addButton(slot++, shapeReplacements.get(c));
                }
            }
        }

        return paginatedInventory;
    }

    private void setNavigator(int slot, CustomInventory inventory, boolean next, int page, int maxPages) {
        if (next)
            setNextPage(slot, inventory, page, maxPages);
        else
            setPreviousPage(slot, inventory, page);
    }

    private void setPreviousPage(int slot, CustomInventory inventory, int page) {
        if (previousPage == null)
            return;

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
    }

    private void setNextPage(int slot, CustomInventory inventory, int page, int maxPages) {
        if (nextPage == null)
            return;

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

    @Nullable
    public Object getData(@NonNull Object key) {
        return initialData.get(key);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();

        if (title != null)
            map.put("title", title);
        if (shape != null)
            map.put("shape", shape);
        if (nextPage != null)
            map.put("next-page", nextPage);
        if (previousPage != null)
            map.put("previous-page", previousPage);
        if (!shapeReplacements.isEmpty())
            map.put("shape-replacements", shapeReplacements);
        if (!content.isEmpty())
            map.put("content", content);
        if (!initialData.isEmpty())
            map.put("initial-data", initialData);

        return map;
    }

    @Override
    public PaginatedInventoryBuilder clone() {
        try {
            return new PaginatedInventoryBuilder(
                    title,
                    shape,
                    ObjectUtil.clone(shapeReplacements),
                    ObjectUtil.clone(content),
                    nextPage == null ? null : nextPage.clone(),
                    previousPage == null ? null : previousPage.clone(),
                    ObjectUtil.clone(listeners),
                    ObjectUtil.clone(initialData)
            );
        } catch (Exception e) {
            return this;
        }
    }

    @SuppressWarnings("unchecked")
    public static PaginatedInventoryBuilder deserialize(Map<String, Object> map) {
        PaginatedInventoryBuilder builder = new PaginatedInventoryBuilder();
        Map<Character, ItemButton> shapeReplacements = MapUtils.mapKeys((Map<String, ItemButton>) map.get("shape-replacements"), key -> key.charAt(0));
        builder
                .title((String) map.get("title"))
                .shape((String) map.get("shape"))
                .shapeReplacements(shapeReplacements)
                .content((List<ItemButton>) map.get("content"))
                .nextPage((InventoryNavigator) map.get("next-page"))
                .previousPage((InventoryNavigator) map.get("previous-page"))
                .initialData((Map<Object, Object>) map.get("initial-data"));
        return builder;
    }
}
