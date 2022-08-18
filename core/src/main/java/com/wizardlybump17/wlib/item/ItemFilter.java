package com.wizardlybump17.wlib.item;

import com.wizardlybump17.wlib.util.MapUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SerializableAs("item-filter")
@RequiredArgsConstructor
public class ItemFilter implements ConfigurationSerializable {

    private static final Pattern STARTS_WITH = Pattern.compile("(.+)\\*");
    private static final Pattern ENDS_WITH = Pattern.compile("\\*(.+)");
    private static final Pattern CONTAINS = Pattern.compile("\\*(.+)\\*");

    private final Map<FilterType, Object> filters;

    /**
     * Tests if any of the items in the collection matches this filter
     * @param items the items to test
     * @return true if any of the items in the collection matches this filter
     */
    public boolean anyPass(Collection<ItemStack> items) {
        for (ItemStack item : items)
            if (accept(item))
                return true;
        return false;
    }

    /**
     * Checks if the item passes to this filter.
     * @param item the item to be tested
     * @return if the item passes to this filter.
     * @see FilterType
     */
    @SuppressWarnings("unchecked")
    public boolean accept(ItemStack item) {
        final ItemBuilder builder = ItemBuilder.fromItemStack(item);

        for (Map.Entry<FilterType, Object> entry : filters.entrySet()) {
            final FilterType type = entry.getKey();
            final Object filter = entry.getValue();

            switch (type) {
                case GLOW -> {
                    if (!testGlow(builder, (boolean) filter))
                        return false;
                }

                case UNBREAKABLE -> {
                    if (!testUnbreakable(builder, (boolean) filter))
                        return false;
                }

                case MATERIAL -> {
                    if (!testMaterial(builder, (String) filter))
                        return false;
                }

                case LORE -> {
                    if (!testLore(builder, (List<String>) filter))
                        return false;
                }

                case DISPLAY_NAME -> {
                    if (!testDisplayName(builder, (String) filter))
                        return false;
                }

                case FLAGS -> {
                    final Collection<String> flags = (Collection<String>) filter;
                    if (flags == null && builder.itemFlags().isEmpty())
                        continue;
                    if (flags != null && !builder.itemFlags().isEmpty()) {
                        if (!builder.itemFlags().stream().map(ItemFlag::name).toList().containsAll(flags))
                            return false;
                        continue;
                    }
                    return false;
                }

                case ENCHANTMENTS -> {
                    final Map<String, Integer> enchantments = (Map<String, Integer>) filter;
                    if (enchantments == null && builder.enchantments().isEmpty())
                        continue;
                    if (enchantments != null && !builder.enchantments().isEmpty()) {
                        if (!MapUtils.contains(MapUtils.mapKeys(builder.enchantments(), enchantment -> enchantment.getKey().toString()), enchantments))
                            return false;
                        continue;
                    }
                    return false;
                }

                case AMOUNT -> {
                    if (filter instanceof Integer) {
                        if (item.getAmount() != (int) filter)
                            return false;
                        continue;
                    }

                    String amount = filter.toString();
                    lessCheck:
                    {
                        Matcher matcher = STARTS_WITH.matcher(amount);
                        if (!matcher.matches())
                            break lessCheck;

                        int value = Integer.parseInt(matcher.group(1));
                        if (item.getAmount() >= value)
                            return false;
                        continue;
                    }

                    equalsCheck:
                    {
                        Matcher matcher = CONTAINS.matcher(amount);
                        if (!matcher.matches())
                            break equalsCheck;

                        int value = Integer.parseInt(matcher.group(1));
                        if (item.getAmount() != value)
                            return false;
                        continue;
                    }

                    greaterCheck:
                    {
                        Matcher matcher = ENDS_WITH.matcher(amount);
                        if (!matcher.matches())
                            break greaterCheck;

                        int value = Integer.parseInt(matcher.group(1));
                        if (item.getAmount() <= value)
                            return false;
                        continue;
                    }

                    return false;
                }
            }
        }

        return true;
    }

    public static boolean testGlow(ItemBuilder item, boolean shouldGlow) {
        return (shouldGlow && item.glow()) || (!shouldGlow && !item.glow());
    }

    public static boolean testUnbreakable(ItemBuilder item, boolean shouldUnbreakable) {
        return (shouldUnbreakable && item.unbreakable()) || (!shouldUnbreakable && !item.unbreakable());
    }

    public static boolean testMaterial(ItemBuilder builder, String material) {
        return test(material.toLowerCase(), builder.type().name().toLowerCase());
    }

    public static boolean testLore(ItemBuilder builder, List<String> lore) {
        if (lore == null && builder.lore().isEmpty())
            return true;
        if (lore != null && !builder.lore().isEmpty())
            return new HashSet<>(builder.lore()).containsAll(lore);
        return false;
    }

    public static boolean testDisplayName(ItemBuilder builder, String displayName) {
        if (builder.displayName().isEmpty() && displayName == null)
            return true;
        if (!builder.displayName().isEmpty() && displayName != null)
            return test(displayName.toLowerCase(), builder.displayName());
        return false;
    }

    @Override
    public Map<String, Object> serialize() {
        return MapUtils.mapOf("filters", MapUtils.mapKeys(filters, type -> type.name().toLowerCase()));
    }

    public static ItemFilter deserialize(Map<String, Object> args) {
        Map<FilterType, Object> filters = new EnumMap<>(FilterType.class);

        for (Map.Entry<String, Object> entry : args.entrySet()) {
            Object value = entry.getValue();
            switch (entry.getKey().toLowerCase()) {
                case "material", "type" -> {
                    if (value != null && !(value instanceof String))
                        notValid("type/material", String.class, value.getClass());
                    filters.put(FilterType.MATERIAL, value);
                }
                case "name", "display-name" -> {
                    if (value != null && !(value instanceof String))
                        notValid("display-name/name", String.class, value.getClass());
                    filters.put(FilterType.DISPLAY_NAME, value);
                }
                case "lore" -> {
                    if (value != null && !(value instanceof List))
                        notValid("lore", List.class, value.getClass());
                    filters.put(FilterType.LORE, value);
                }
                case "item-flags", "flags" -> {
                    if (value != null && !(value instanceof List))
                        notValid("flags/item-flags", List.class, value.getClass());
                    filters.put(FilterType.FLAGS, value);
                }
                case "unbreakable" -> {
                    if (value != null && !(value instanceof Boolean))
                        notValid("unbreakable", boolean.class, value.getClass());
                    filters.put(FilterType.UNBREAKABLE, value);
                }
                case "glow" -> {
                    if (value != null && !(value instanceof Boolean))
                        notValid("glow", boolean.class, value.getClass());
                    filters.put(FilterType.GLOW, value);
                }
                case "enchantment", "enchantments" -> {
                    if (value != null && !(value instanceof Map))
                        notValid("enchantments/enchantment", Map.class, value.getClass());
                    filters.put(FilterType.ENCHANTMENTS, value);
                }
                case "amount" -> {
                    if (value != null && !(value instanceof String || value instanceof Integer))
                        notValid("amount", String.class, value.getClass());
                    filters.put(FilterType.AMOUNT, value);
                }
            }
        }

        return new ItemFilter(filters);
    }

    private static void notValid(String type, Class<?> expected, Class<?> got) {
        throw new IllegalArgumentException("invalid value of filter type \"" + type + "\": expected " + expected.getName() + " but got " + got.getName());
    }

    private static boolean test(String filter, String string) {
        final Matcher containsMatcher = CONTAINS.matcher(filter);
        if (containsMatcher.matches())
            return string.contains(containsMatcher.group(1));

        final Matcher startMatcher = STARTS_WITH.matcher(filter);
        if (startMatcher.matches())
            return string.startsWith(startMatcher.group(1));

        final Matcher endMatcher = ENDS_WITH.matcher(filter);
        if (endMatcher.matches())
            return string.endsWith(endMatcher.group(1));

        return string.equals(filter);
    }
}
