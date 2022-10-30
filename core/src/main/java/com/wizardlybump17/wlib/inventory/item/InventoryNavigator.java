package com.wizardlybump17.wlib.inventory.item;

import com.wizardlybump17.wlib.item.ItemBuilder;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@SerializableAs("inventory-navigator")
public class InventoryNavigator implements ConfigurationSerializable, Cloneable {

    public static final InventoryNavigator NEXT_PAGE = new InventoryNavigator(
            new ItemBuilder()
                    .type(Material.ARROW)
                    .displayName("§aNext Page")
                    .build(),
            '#'
    );
    public static final InventoryNavigator PREVIOUS_PAGE = new InventoryNavigator(
            new ItemBuilder()
                    .type(Material.ARROW)
                    .displayName("§aPrevious Page")
                    .build(),
            '#'
    );

    private final ItemStack item;
    @Nullable
    private final Character replacementChar;
    @Nullable
    private final ItemButton replacementButton;

    public InventoryNavigator(ItemStack item) {
        this(item, null, null);
    }

    public InventoryNavigator(ItemStack item, Character replacementChar) {
        this(item, replacementChar, null);
    }

    public InventoryNavigator(ItemStack item, ItemButton replacementButton) {
        this(item, null, replacementButton);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("item", ItemBuilder.fromItemStack(item).clone());
        if (replacementChar != null)
            map.put("replacement-char", replacementChar);
        if (replacementButton != null)
            map.put("replacement-button", replacementButton);
        return map;
    }

    @Override
    public InventoryNavigator clone() {
        return new InventoryNavigator(
                item.clone(),
                replacementChar,
                replacementButton == null ? null : replacementButton.clone()
        );
    }

    public static InventoryNavigator deserialize(Map<String, Object> args) {
        return new InventoryNavigator(
                ((ItemBuilder) args.get("item")).build(),
                args.get("replacement-char") == null ? null : args.get("replacement-char").toString().charAt(0),
                (ItemButton) args.get("replacement-button")
        );
    }
}
