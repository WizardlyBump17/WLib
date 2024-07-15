package com.wizardlybump17.wlib.command.reader;

import com.wizardlybump17.wlib.command.args.reader.ArgsReader;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.Nullable;

public class EnchantmentReader extends ArgsReader<Enchantment> {

    @Override
    public @Nullable Class<Enchantment> getType() {
        return Enchantment.class;
    }

    @Override
    public Enchantment read(String string) {
        NamespacedKey key = NamespacedKey.fromString(string);
        return key == null ? null : Registry.ENCHANTMENT.get(key);
    }
}
