package com.wizardlybump17.wlib.item.enchantment;

import com.wizardlybump17.wlib.WLib;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class GlowEnchantment extends Enchantment {

    public static final GlowEnchantment INSTANCE = new GlowEnchantment();

    public GlowEnchantment() {
        super(new NamespacedKey(WLib.getInstance(), "glow"));
    }

    @NotNull
    @Override
    public String getName() {
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

    @NotNull
    @Override
    public EnchantmentTarget getItemTarget() {
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
    public boolean conflictsWith(@NotNull Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack itemStack) {
        return true;
    }

    public static void register() {
        if (Enchantment.getByKey(INSTANCE.getKey()) != null)
            return;

        try {
            Field field = Enchantment.class.getDeclaredField("acceptingNew");
            field.setAccessible(true);
            field.set(null, true);
            Enchantment.registerEnchantment(INSTANCE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
