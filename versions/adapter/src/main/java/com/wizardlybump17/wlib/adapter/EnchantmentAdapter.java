package com.wizardlybump17.wlib.adapter;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.Nullable;

public class EnchantmentAdapter {

    @Getter
    private static EnchantmentAdapter instance;

    public static void setInstance(@NonNull EnchantmentAdapter instance) {
        if (EnchantmentAdapter.instance != null)
            throw new IllegalStateException("The instance is already set");
        EnchantmentAdapter.instance = instance;
    }

    public @Nullable Enchantment getEnchantment(@NonNull String string) {
        return Enchantment.getByKey(NamespacedKey.fromString(string));
    }
}
