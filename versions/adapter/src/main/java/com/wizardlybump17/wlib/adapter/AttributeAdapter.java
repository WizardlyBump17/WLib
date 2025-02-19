package com.wizardlybump17.wlib.adapter;

import com.google.common.collect.Multimap;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class AttributeAdapter {

    private static AttributeAdapter instance;

    public abstract Attribute getAttribute(@NotNull String name);

    public abstract @NotNull Map<String, Object> serialize(@NotNull Multimap<Attribute, AttributeModifier> attributes);

    public abstract @NotNull Multimap<Attribute, AttributeModifier> deserialize(@NotNull Map<String, ? extends Object> serialized);

    public static AttributeAdapter getInstance() {
        return instance;
    }

    public static void setInstance(@NotNull AttributeAdapter instance) {
        if (AttributeAdapter.instance != null)
            throw new IllegalStateException("The AttributeAdapter is already set");
        AttributeAdapter.instance = instance;
    }
}
