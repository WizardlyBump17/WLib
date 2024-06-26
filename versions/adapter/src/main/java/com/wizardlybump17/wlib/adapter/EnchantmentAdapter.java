package com.wizardlybump17.wlib.adapter;

import com.wizardlybump17.wlib.util.ReflectionUtil;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Map;

public class EnchantmentAdapter {

    public static final @NonNull Field ACCEPTING_NEW = ReflectionUtil.getField("acceptingNew", Enchantment.class);
    public static final @NonNull Field BY_KEY = ReflectionUtil.getField("byKey", Enchantment.class);
    public static final @NonNull Field BY_NAME = ReflectionUtil.getField("byName", Enchantment.class);

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

    public boolean registerEnchantment(@NonNull Enchantment enchantment) {
        if (Enchantment.getByKey(enchantment.getKey()) != null || Enchantment.getByName(enchantment.getName()) != null)
            return false;

        ReflectionUtil.setFieldValue(ACCEPTING_NEW, null, true);
        Enchantment.registerEnchantment(enchantment);
        return true;
    }

    public boolean unregisterEnchantment(@NonNull Enchantment enchantment) {
        if (Enchantment.getByKey(enchantment.getKey()) == null)
            return false;

        Map<NamespacedKey, Enchantment> byKey = ReflectionUtil.getFieldValue(BY_KEY, null);
        Map<String, Enchantment> byName = ReflectionUtil.getFieldValue(BY_NAME, null);

        byKey.remove(enchantment.getKey());
        byName.remove(enchantment.getName());

        return true;
    }
}
