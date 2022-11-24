package com.wizardlybump17.wlib.item;

import com.wizardlybump17.wlib.util.MapUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SerializableAs("item-filter")
@RequiredArgsConstructor
public class ItemFilter implements ConfigurationSerializable {

    private static final Pattern STARTS_WITH = Pattern.compile("(.+)\\*");
    private static final Pattern ENDS_WITH = Pattern.compile("\\*(.+)");
    private static final Pattern CONTAINS = Pattern.compile("\\*(.+)\\*");

    private final List<Map<FilterType, Object>> filters;

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
        for (Map<FilterType, Object> filtersMap : filters) {
            for (Map.Entry<FilterType, Object> entry : filtersMap.entrySet()) {
                Object object = entry.getValue();
                switch (entry.getKey()) {
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
                if (!testString(line, builder.lore()))
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
                if (!testString(flag, flags))
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
            Matcher containsMatcher = CONTAINS.matcher(base);
            if (containsMatcher.matches())
                return string.contains(containsMatcher.group(1));

            Matcher startMatcher = STARTS_WITH.matcher(base);
            if (startMatcher.matches())
                return string.startsWith(startMatcher.group(1));

            Matcher endMatcher = ENDS_WITH.matcher(base);
            if (endMatcher.matches())
                return string.endsWith(endMatcher.group(1));

            return string.equals(base);
        }

        return false;
    }

    public static boolean testString(String base, Iterable<String> strings) {
        for (String string : strings)
            if (testString(base, string))
                return true;
        return false;
    }

    public static boolean testInt(String base, int i) {
        Matcher containsMatcher = CONTAINS.matcher(base);
        if (containsMatcher.matches()) {
            int value = Integer.parseInt(containsMatcher.group(1));
            return i == value;
        }

        Matcher startsMatcher = STARTS_WITH.matcher(base);
        if (startsMatcher.matches()) {
            int value = Integer.parseInt(startsMatcher.group(1));
            return i >= value;
        }

        Matcher endsMatcher = ENDS_WITH.matcher(base);
        if (endsMatcher.matches()) {
            int value = Integer.parseInt(endsMatcher.group(1));
            return i <= value;
        }

        return false;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of("filters", filters.stream().map(map -> MapUtils.mapKeys(map, Enum::name)).toList());
    }

    @SuppressWarnings("unchecked")
    public static ItemFilter deserialize(Map<String, Object> args) {
        List<Map<FilterType, Object>> filters = new ArrayList<>();
        for (Map<String, Object> map : ((List<Map<String, Object>>) args.get("filters")))
            filters.add(MapUtils.mapKeys(map, key -> FilterType.valueOf(key.toUpperCase())));
        return new ItemFilter(filters);
    }
}
