package com.wizardlybump17.wlib.inventory.item;

import com.wizardlybump17.wlib.item.ItemBuilder;
import com.wizardlybump17.wlib.object.Pair;
import lombok.Data;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Data
public class ItemButton {

    public static final ItemButton BLACK_STAINED_GLASS_PANE = new ItemButton(
            new ItemBuilder()
                    .type(Material.BLACK_STAINED_GLASS_PANE)
                    .displayName(" ")
                    .build()
    );
    public static final ItemButton AIR = new ItemButton(new ItemStack(Material.AIR));

    @NotNull
    private final Supplier<ItemStack> item;
    @Nullable
    private final ClickAction clickAction;
    @NonNull
    private final Map<Object, Object> customData;

    public ItemButton(@NotNull ItemStack item) {
        this(item, null, new HashMap<>());
    }

    public ItemButton(@NotNull ItemStack item, @Nullable ClickAction clickAction) {
        this(() -> item, clickAction, new HashMap<>());
    }

    public ItemButton(@NotNull ItemStack item, @Nullable ClickAction clickAction, @NonNull Map<Object, Object> customData) {
        this(() -> item, clickAction, customData);
    }

    public ItemButton(@NotNull Supplier<ItemStack> item) {
        this(item, null, new HashMap<>());
    }

    public ItemButton(@NotNull Supplier<ItemStack> item, @Nullable ClickAction clickAction) {
        this(item, clickAction, new HashMap<>());
    }

    public ItemButton(@NotNull Supplier<ItemStack> item, @Nullable ClickAction clickAction, @NonNull Map<Object, Object> customData) {
        this.item = item;
        this.clickAction = clickAction;
        this.customData = customData;
    }

    public ItemButton(@NotNull Pair<ItemStack, ClickAction> pair) {
        this(pair::getFirst, pair.getSecond(), new HashMap<>());
    }

    public ItemButton(@NotNull Pair<ItemStack, ClickAction> pair, @NonNull Map<Object, Object> customData) {
        this(pair::getFirst, pair.getSecond(), customData);
    }

    public ItemButton(@NonNull ItemBuilder itemBuilder) {
        this(itemBuilder::build, null, itemBuilder.customData());
    }

    public ItemButton(@NonNull ItemBuilder itemBuilder, @Nullable ClickAction clickAction) {
        this(itemBuilder::build, clickAction, itemBuilder.customData());
    }

    public ItemButton(@NonNull ItemBuilder itemBuilder, @Nullable ClickAction clickAction, @NonNull Map<Object, Object> customData) {
        this(itemBuilder::build, clickAction, customData);
    }
}
