package com.wizardlybump17.wlib.adapter;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Map;

public abstract class ItemAdapter {

    public static final PersistentDataAdapterContext PERSISTENT_DATA_ADAPTER_CONTEXT = new ItemStack(Material.BOW).getItemMeta().getPersistentDataContainer().getAdapterContext();;
    private static ItemAdapter instance;

    public static ItemAdapter getInstance() {
        return instance;
    }

    public static void setInstance(ItemAdapter instance) {
        if (ItemAdapter.instance == null)
            ItemAdapter.instance = instance;
    }

    public abstract Map<Object, Object> serializeContainer(PersistentDataContainer container);

    public abstract PersistentDataContainer deserializeContainer(Map<Object, Object> map);

    public abstract void transferPersistentData(PersistentDataContainer from, PersistentDataContainer to);
}
