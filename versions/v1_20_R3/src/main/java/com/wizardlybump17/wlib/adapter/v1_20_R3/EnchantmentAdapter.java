package com.wizardlybump17.wlib.adapter.v1_20_R3;

import lombok.NonNull;
import org.bukkit.enchantments.Enchantment;

public class EnchantmentAdapter extends com.wizardlybump17.wlib.adapter.EnchantmentAdapter {

    @Override
    public boolean registerEnchantment(@NonNull Enchantment enchantment) {
        throw new UnsupportedOperationException("This method is not supported in this version.");
    }

    @Override
    public boolean unregisterEnchantment(@NonNull Enchantment enchantment) {
        throw new UnsupportedOperationException("This method is not supported in this version.");
    }
}
