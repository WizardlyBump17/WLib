package com.wizardlybump17.wlib.inventory.item;

import com.wizardlybump17.wlib.item.ItemBuilder;
import com.wizardlybump17.wlib.object.Pair;
import com.wizardlybump17.wlib.util.ObjectUtil;
import lombok.Data;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

@Data
@SerializableAs("item-button")
public class ItemButton implements ConfigurationSerializable, Cloneable {

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

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("item", ItemBuilder.fromItemStack(item.get()));
        if (!customData.isEmpty())
            map.put("custom-data", customData);
        return map;
    }

    @Override
    public ItemButton clone() {
        return new ItemButton(
                ObjectUtil.clone(item.get()),
                clickAction,
                ObjectUtil.clone(customData)
        );
    }

    @SuppressWarnings("unchecked")
    public static ItemButton deserialize(Map<String, Object> map) {
        ItemBuilder itemBuilder = (ItemBuilder) map.get("item");
        Map<Object, Object> customData = (Map<Object, Object>) map.get("custom-data");
        return new ItemButton(itemBuilder, null, customData == null ? new HashMap<>() : customData);
    }
}
