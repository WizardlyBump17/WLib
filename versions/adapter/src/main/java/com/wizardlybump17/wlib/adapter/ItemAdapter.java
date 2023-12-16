package com.wizardlybump17.wlib.adapter;

import com.wizardlybump17.wlib.util.ReflectionUtil;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Base64;
import java.util.Collections;
import java.util.Map;

public abstract class ItemAdapter {

    public static final PersistentDataAdapterContext PERSISTENT_DATA_ADAPTER_CONTEXT = new ItemStack(Material.BOW).getItemMeta().getPersistentDataContainer().getAdapterContext();
    private static ItemAdapter instance;

    public static ItemAdapter getInstance() {
        return instance;
    }

    public static void setInstance(ItemAdapter instance) {
        if (ItemAdapter.instance == null)
            ItemAdapter.instance = instance;
    }

    public abstract Map<String, Object> serializeContainer(PersistentDataContainer container);

    public abstract PersistentDataContainer deserializeContainer(Map<String, Object> map);

    public abstract void transferPersistentData(PersistentDataContainer from, PersistentDataContainer to);

    public abstract void copyPersistentData(PersistentDataContainer from, PersistentDataContainer to);

    public abstract void setSkull(SkullMeta meta, String url);

    public abstract String getSkullUrl(SkullMeta meta);

    public @NonNull ItemStack setRawNBTTag(@NonNull ItemStack item, @NonNull String key, @NonNull Object value) {
        return item;
    }

    public @NonNull ItemStack setRawNBTTags(@NonNull ItemStack item, @NonNull Map<String, Object> tags) {
        return item;
    }

    public @NonNull Map<String, Object> getRawNBTTags(@NonNull ItemStack item) {
        return Collections.emptyMap();
    }

    public @NonNull Enchantment getGlowEnchantment() {
        return GlowEnchantment.INSTANCE;
    }

    public void registerGlowEnchantment() {
        Enchantment enchantment = getGlowEnchantment();

        if (Enchantment.getByKey(enchantment.getKey()) != null)
            return;

        ReflectionUtil.setFieldValue(ReflectionUtil.getField("acceptingNew", Enchantment.class), null, true);
        Enchantment.registerEnchantment(enchantment);
    }

    public static String getSkullTextureBase64(String url) {
        return new String(Base64.getEncoder().encode(("{textures:{SKIN:{url:\"" + url + "\"}}}").getBytes()));
    }
}
