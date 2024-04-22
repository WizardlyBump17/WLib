package com.wizardlybump17.wlib.command.reader;

import com.wizardlybump17.wlib.adapter.EnchantmentAdapter;
import com.wizardlybump17.wlib.command.args.reader.ArgsReader;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.Nullable;

public class EnchantmentReader extends ArgsReader<Enchantment> {

    @Override
    public @Nullable Class<Enchantment> getType() {
        return Enchantment.class;
    }

    @Override
    public Enchantment read(String string) {
        return EnchantmentAdapter.getInstance().getEnchantment(string);
    }
}
