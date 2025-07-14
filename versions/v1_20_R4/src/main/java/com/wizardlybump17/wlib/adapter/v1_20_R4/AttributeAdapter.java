package com.wizardlybump17.wlib.adapter.v1_20_R4;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AttributeAdapter extends com.wizardlybump17.wlib.adapter.AttributeAdapter {

    @Override
    public Attribute getAttribute(@NotNull String name) {
        try {
            return Attribute.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException ignored) {
            return Registry.ATTRIBUTE.get(NamespacedKey.fromString(name.toLowerCase()));
        }
    }

    @Override
    public @NotNull Map<String, Object> serialize(@NotNull Multimap<Attribute, AttributeModifier> attributes) {
        Map<String, Object> result = new TreeMap<>();
        for (Attribute attribute : attributes.keySet())
            result.put(attribute.getKey().toString(), List.copyOf(attributes.get(attribute)));
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> deserialize(@NotNull Map<String, ? extends Object> serialized) {
        Multimap<Attribute, AttributeModifier> result = TreeMultimap.create(
                Comparator.comparing(attribute -> attribute.getKey().toString()),
                Comparator.<AttributeModifier, String>comparing(modifier -> modifier.getUniqueId().toString())
                        .thenComparing(AttributeModifier::getAmount)
                        .thenComparing(AttributeModifier::getOperation)
        );
        serialized.forEach((attribute, modifiers) -> {
            if (modifiers != null)
                for (AttributeModifier modifier : ((Collection<AttributeModifier>) modifiers))
                    result.put(getAttribute(attribute), modifier);
        });
        return result;
    }
}
