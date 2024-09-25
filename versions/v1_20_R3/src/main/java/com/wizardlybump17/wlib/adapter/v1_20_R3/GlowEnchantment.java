package com.wizardlybump17.wlib.adapter.v1_20_R3;

import lombok.NonNull;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class GlowEnchantment extends Enchantment {

    public static final @NonNull NamespacedKey KEY = new NamespacedKey("wlib", "glow");
    public static final @NonNull GlowEnchantment INSTANCE = new GlowEnchantment();

    private GlowEnchantment() {
    }

    @Override
    public @NonNull String getName() {
        return "glow";
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public int getStartLevel() {
        return 0;
    }

    @Override
    public @NonNull EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ARMOR;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(@NonNull Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(@NonNull ItemStack itemStack) {
        return true;
    }

    @Override
    public @NonNull NamespacedKey getKey() {
        return KEY;
    }

    @Override
    public @NonNull String getTranslationKey() {
        return "wlib.glow";
    }
}
