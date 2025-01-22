package com.wizardlybump17.wlib.adapter;

import org.bukkit.attribute.Attribute;
import org.jetbrains.annotations.NotNull;

public class AttributeAdapter {

    private static AttributeAdapter instance;

    public Attribute getAttribute(@NotNull String name) {
        return Attribute.valueOf(name.toUpperCase());
    }

    public static AttributeAdapter getInstance() {
        return instance;
    }

    public static void setInstance(@NotNull AttributeAdapter instance) {
        if (AttributeAdapter.instance != null)
            throw new IllegalStateException("The AttributeAdapter is already set");
        AttributeAdapter.instance = instance;
    }
}
