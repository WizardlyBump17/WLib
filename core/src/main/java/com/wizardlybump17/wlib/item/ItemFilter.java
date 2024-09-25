package com.wizardlybump17.wlib.item;

import com.wizardlybump17.wlib.util.MapUtils;
import com.wizardlybump17.wlib.util.bukkit.ConfigUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SerializableAs("WLib:ItemFilter")
@RequiredArgsConstructor
@Getter
public class ItemFilter implements ConfigurationSerializable {

    private final Map<FilterType, Object> filters;

    /**
     * @param items the items to test
     * @return if any of the items in the iterable is accepted by this filter
     */
    public boolean testAny(Iterable<ItemStack> items) {
        for (ItemStack item : items)
            if (accept(item))
                return true;
        return false;
    }

    /**
     * @param items the items to test
     * @return if all the items in the iterable are accepted by this filter
     */
    public boolean testAll(Iterable<ItemStack> items) {
        for (ItemStack item : items)
            if (!accept(item))
                return false;
        return true;
    }

    /**
     * @param item the item to be tested
     * @return if the item is accepted by this filter
     * @see FilterType
     */
    @SuppressWarnings("unchecked")
    public boolean accept(ItemStack item) {
        ItemBuilder builder = ItemBuilder.fromItemStack(item);
        for (Map.Entry<FilterType, Object> entry : filters.entrySet()) {
            FilterType type = entry.getKey();
            Object object = entry.getValue();

            switch (type) {
                case MATERIAL -> {
                    if (!testMaterial(object.toString(), builder))
                        return false;
                }
                case AMOUNT -> {
                    if (!testAmount(object.toString(), builder))
                        return false;
                }
                case DAMAGE -> {
                    if (!testDamage(object.toString(), builder))
                        return false;
                }
                case DISPLAY_NAME -> {
                    if (!testDisplayName(object.toString(), builder))
                        return false;
                }
                case LORE -> {
                    if (!testLore((List<String>) object, builder))
                        return false;
                }
                case ENCHANTMENTS -> {
                    if (!testEnchantments((Map<String, String>) object, builder))
                        return false;
                }
                case ITEM_FLAGS -> {
                    if (!testItemFlags((List<String>) object, builder))
                        return false;
                }
                case GLOW -> {
                    if (!testGlow((boolean) object, builder))
                        return false;
                }
                case UNBREAKABLE -> {
                    if (!testUnbreakable((boolean) object, builder))
                        return false;
                }
                case NBT_TAGS -> {
                    if (!testNbtTags((Map<String, String>) object, builder))
                        return false;
                }
            }
        }

        return true;
    }

    public int first(ItemStack[] items) {
        for (int i = 0; i < items.length; i++)
            if (accept(items[i]))
                return i;
        return -1;
    }

    public int first(List<ItemStack> items) {
        for (int i = 0; i < items.size(); i++)
            if (accept(items.get(i)))
                return i;
        return -1;
    }

    public static boolean testMaterial(String material, ItemBuilder builder) {
        return testString(material.toLowerCase(), builder.type().name().toLowerCase());
    }

    public static boolean testAmount(String amount, ItemBuilder builder) {
        return testInt(amount, builder.amount());
    }

    public static boolean testDamage(String damage, ItemBuilder builder) {
        return testInt(damage, builder.damage());
    }

    public static boolean testDisplayName(String displayName, ItemBuilder builder) {
        return testString(displayName, builder.displayName());
    }

    public static boolean testLore(List<String> lore, ItemBuilder builder) {
        if (lore == null && builder.lore().isEmpty())
            return true;

        if (lore != null && !builder.lore().isEmpty()) {
            for (String line : lore)
                if (!testStrings(line, builder.lore()))
                    return false;
            return true;
        }

        return false;
    }

    public static boolean testEnchantments(Map<String, String> enchantments, ItemBuilder builder) {
        if (enchantments == null && builder.enchantments().isEmpty())
            return true;

        if (enchantments != null && !builder.enchantments().isEmpty()) {
            for (Map.Entry<String, String> baseEntry : enchantments.entrySet())
                for (Map.Entry<Enchantment, Integer> enchantmentEntry : builder.enchantments().entrySet())
                    if (!testString(baseEntry.getKey(), enchantmentEntry.getKey().getKey().toString()) || !testInt(baseEntry.getValue(), enchantmentEntry.getValue()))
                        return false;

            return true;
        }

        return false;
    }

    public static boolean testItemFlags(List<String> itemFlags, ItemBuilder builder) {
        Set<String> flags = builder.itemFlags().stream().map(Enum::name).collect(Collectors.toSet());
        if (itemFlags == null && flags.isEmpty())
            return true;

        if (itemFlags != null && !flags.isEmpty()) {
            for (String flag : itemFlags)
                if (!testStrings(flag, flags))
                    return false;
            return true;
        }

        return false;
    }

    public static boolean testGlow(boolean glow, ItemBuilder item) {
        return (glow && item.glow()) || (!glow && !item.glow());
    }

    public static boolean testUnbreakable(boolean unbreakable, ItemBuilder item) {
        return (unbreakable && item.unbreakable()) || (!unbreakable && !item.unbreakable());
    }

    public static boolean testNbtTags(Map<String, String> tags, ItemBuilder builder) {
        if (tags == null && builder.nbtTags().isEmpty())
            return true;

        if (tags != null && !builder.nbtTags().isEmpty()) {
            for (Map.Entry<String, String> baseEntry : tags.entrySet())
                for (Map.Entry<String, Object> tagEntry : builder.nbtTags().entrySet())
                    if (!testString(baseEntry.getKey(), tagEntry.getKey()) || !testString(baseEntry.getValue(), tagEntry.getValue().toString()))
                        return false;

            return true;
        }

        return false;
    }

    public static boolean testString(String base, String string) {
        if (base.isEmpty() && string == null)
            return true;

        if (!base.isEmpty() && string != null) {
            if (base.charAt(0) == '*' && base.charAt(base.length() - 1) == '*') //contains
                return string.contains(base.substring(1, base.length() - 2));

            if (base.charAt(0) == '*') //starts with
                return string.startsWith(base.substring(1));

            if (base.charAt(base.length() - 1) == '*') //ends with
                return string.endsWith(base.substring(0, base.length() - 2));

            return string.equals(base);
        }

        return false;
    }

    public static boolean testStrings(String base, Iterable<String> strings) {
        for (String string : strings)
            if (testString(base, string))
                return true;
        return false;
    }

    public static boolean testInt(String base, int i) {
        if (base.charAt(0) == '=') { //equals
            int value = Integer.parseInt(base.substring(1));
            return i == value;
        }

        if (base.charAt(0) == '>') { //greater than
            int value = Integer.parseInt(base.substring(1));
            return i >= value;
        }

        if (base.charAt(base.length() - 1) == '<') { //less than
            int value = Integer.parseInt(base.substring(0, base.length() - 2));
            return i <= value;
        }

        return false;
    }

    @Override
    public @NonNull Map<String, Object> serialize() {
        return Map.of("filters", MapUtils.mapKeys(filters, Enum::name));
    }

    public static ItemFilter deserialize(@NonNull Map<String, Object> map) {
        return new ItemFilter(
                ConfigUtil.<Map<String, Object>, Map<FilterType, Object>>map(
                        "filters",
                        map,
                        filters -> MapUtils.mapKeys(
                                filters,
                                () -> new EnumMap<>(FilterType.class),
                                filter -> FilterType.valueOf(filter.toUpperCase())
                        )
                )
        );
    }
}
