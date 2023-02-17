package com.wizardlybump17.wlib.adapter;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Base64;
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

    public static String getSkullTextureBase64(String url) {
        return new String(Base64.getEncoder().encode(("{textures:{SKIN:{url:\"" + url + "\"}}}").getBytes()));
    }
}
