package com.wizardlybump17.wlib.inventory.item;

import com.wizardlybump17.wlib.item.ItemBuilder;
import com.wizardlybump17.wlib.object.Pair;
import com.wizardlybump17.wlib.util.bukkit.ConfigUtil;
import com.wizardlybump17.wlib.util.bukkit.config.ConfigSound;
import lombok.Data;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
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
    private Supplier<ItemStack> item;
    @Nullable
    private ClickAction clickAction;
    @NonNull
    private Map<Object, Object> customData;
    private @NonNull List<ConfigSound> clickSounds;

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
        this(item, clickAction, customData, new ArrayList<>());
    }

    public ItemButton(@NotNull Supplier<ItemStack> item, @Nullable ClickAction clickAction, @NonNull Map<Object, Object> customData, @NonNull List<ConfigSound> clickSounds) {
        this.item = item;
        this.clickAction = clickAction;
        this.customData = customData;
        this.clickSounds = clickSounds;
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

    public ItemButton(@NonNull ItemBuilder itemBuilder, @Nullable ClickAction clickAction, @NonNull Map<Object, Object> customData, @NonNull List<ConfigSound> clickSounds) {
        this(itemBuilder::build, clickAction, customData, clickSounds);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("item", ItemBuilder.fromItemStack(item.get()));
        if (!customData.isEmpty())
            map.put("custom-data", customData);
        if (!clickSounds.isEmpty())
            map.put("click-sounds", clickSounds);
        return map;
    }

    @Override
    public ItemButton clone() {
        return new ItemButton(
                item.get()::clone,
                clickAction,
                new HashMap<>(customData),
                new ArrayList<>(clickSounds)
        );
    }

    @SuppressWarnings("unchecked")
    public static ItemButton deserialize(Map<String, Object> map) {
        ItemBuilder itemBuilder = (ItemBuilder) map.get("item");
        Map<Object, Object> customData = (Map<Object, Object>) map.get("custom-data");
        return new ItemButton(
                itemBuilder,
                null,
                customData == null ? new HashMap<>() : customData,
                ConfigUtil.get("click-sounds", map, ArrayList::new)
        );
    }
}
