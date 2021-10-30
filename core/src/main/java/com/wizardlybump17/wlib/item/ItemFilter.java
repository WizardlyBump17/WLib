package com.wizardlybump17.wlib.item;

import com.wizardlybump17.wlib.util.MapUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SerializableAs("item-filter")
@RequiredArgsConstructor
public class ItemFilter implements ConfigurationSerializable {

    private static final Pattern STARTS_WITH = Pattern.compile("(.+)\\*");
    private static final Pattern ENDS_WITH = Pattern.compile("\\*(.+)");
    private static final Pattern CONTAINS = Pattern.compile("\\*(.+)\\*");

    private final Map<FilterType, Object> filters;

    /**
     * Checks if the item passes to this filter.
     * @param item the item to be tested
     * @return if the item passes to this filter.
     * @see FilterType
     */
    @SuppressWarnings("unchecked")
    public boolean accept(ItemStack item) {
        final Item.ItemBuilder builder = Item.fromItemStack(item);

        for (Map.Entry<FilterType, Object> entry : filters.entrySet()) {
            final FilterType type = entry.getKey();
            final Object filter = entry.getValue();

            switch (type) {
                case GLOW: {
                    final boolean glow = (boolean) filter;
                    if ((glow && builder.hasGlow()) || (!glow && !builder.hasGlow()))
                        continue;
                    return false;
                }

                case UNBREAKABLE: {
                    final boolean unbreakable = (boolean) filter;
                    if ((unbreakable && builder.isUnbreakable()) || (!unbreakable && !builder.isUnbreakable()))
                        continue;
                    return false;
                }

                case MATERIAL: {
                    final String material = filter.toString();
                    if (!test(material.toLowerCase(), builder.getType().name().toLowerCase()))
                        return false;
                    continue;
                }

                case LORE: {
                    final List<String> lore = (List<String>) filter;
                    if (lore == null && builder.getLore().isEmpty())
                        continue;
                    if (lore != null && !builder.getLore().isEmpty()) {
                        if (!builder.getLore().containsAll(lore))
                            return false;
                        continue;
                    }
                    return false;
                }

                case DISPLAY_NAME: {
                    final String displayName = (String) filter;
                    if (builder.getDisplayName() == null && displayName == null)
                        continue;
                    if (builder.getDisplayName() != null && displayName != null) {
                        if (!test(displayName.toLowerCase(), builder.getDisplayName()))
                            return false;
                        continue;
                    }
                    return false;
                }

                case FLAGS: {
                    final Collection<String> flags = (Collection<String>) filter;
                    if (flags == null && builder.getFlags().isEmpty())
                        continue;
                    if (flags != null && !builder.getFlags().isEmpty()) {
                        if (!builder.getFlags().stream().map(ItemFlag::name).collect(Collectors.toList()).containsAll(flags))
                            return false;
                        continue;
                    }
                    return false;
                }

                case ENCHANTMENTS: {
                    final Map<String, Integer> enchantments = (Map<String, Integer>) filter;
                    if (enchantments == null && builder.getEnchantments().isEmpty())
                        continue;
                    if (enchantments != null && !builder.getEnchantments().isEmpty()) {
                        if (!MapUtils.contains(MapUtils.mapKeys(builder.getEnchantments(), Enchantment::getName), enchantments))
                            return false;
                        continue;
                    }
                    return false;
                }

                case NBT_TAGS: {
                    final Map<String, Object> nbtTags = (Map<String, Object>) filter;
                    if (nbtTags == null && builder.getNbtTags().isEmpty())
                        continue;
                    if (nbtTags != null && !builder.getNbtTags().isEmpty()) {
                        if (!MapUtils.contains(builder.getNbtTags(), nbtTags))
                            return false;
                        continue;
                    }
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public Map<String, Object> serialize() {
        return MapUtils.mapOf("filters", MapUtils.mapKeys(filters, type -> type.name().toLowerCase()));
    }

    @SuppressWarnings("unchecked")
    public static ItemFilter deserialize(Map<String, Object> args) {
        Map<FilterType, Object> filters = new HashMap<>();

        for (Map.Entry<String, Object> entry : ((Map<String, Object>) args.get("filters")).entrySet()) {
            final Object value = entry.getValue();
            switch (entry.getKey().toLowerCase()) {
                case "material":
                case "type": {
                    if (value != null && !(value instanceof String))
                        notValid("type/material", String.class, value.getClass());
                    filters.put(FilterType.MATERIAL, value);
                    continue;
                }

                case "name":
                case "display-name": {
                    if (value != null && !(value instanceof String))
                        notValid("display-name/name", String.class, value.getClass());
                    filters.put(FilterType.DISPLAY_NAME, value);
                    continue;
                }

                case "lore": {
                    if (value != null && !(value instanceof List))
                        notValid("lore", List.class, value.getClass());
                    filters.put(FilterType.LORE, value);
                    continue;
                }

                case "item-flags":
                case "flags": {
                    if (value != null && !(value instanceof List))
                        notValid("flags/item-flags", List.class, value.getClass());
                    filters.put(FilterType.FLAGS, value);
                    continue;
                }

                case "unbreakable": {
                    if (value != null && !(value instanceof Boolean))
                        notValid("unbreakable", boolean.class, value.getClass());
                    filters.put(FilterType.UNBREAKABLE, value);
                    continue;
                }

                case "glow": {
                    if (value != null && !(value instanceof Boolean))
                        notValid("glow", boolean.class, value.getClass());
                    filters.put(FilterType.GLOW, value);
                    continue;
                }

                case "enchantment":
                case "enchantments": {
                    if (value != null && !(value instanceof Map))
                        notValid("enchantments/enchantment", Map.class, value.getClass());
                    filters.put(FilterType.ENCHANTMENTS, value);
                    continue;
                }

                case "tags":
                case "nbt-tags": {
                    if (value != null && !(value instanceof Map))
                        notValid("nbt-tags/tags", Map.class, value.getClass());
                    filters.put(FilterType.NBT_TAGS, value);
                    continue;
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
            return string.startsWith(containsMatcher.group(1));

        final Matcher startMatcher = STARTS_WITH.matcher(filter);
        if (startMatcher.matches())
            return string.startsWith(startMatcher.group(1));

        final Matcher endMatcher = ENDS_WITH.matcher(filter);
        if (endMatcher.matches())
            return string.startsWith(endMatcher.group(1));

        return string.equals(filter);
    }
}
