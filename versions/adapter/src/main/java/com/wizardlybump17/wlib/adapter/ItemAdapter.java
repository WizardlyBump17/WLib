package com.wizardlybump17.wlib.adapter;

import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    public abstract void transferPersistentData(@NonNull PersistentDataContainer from, @NonNull PersistentDataContainer to);

    public abstract void copyPersistentData(@NonNull PersistentDataContainer from, @NonNull PersistentDataContainer to);

    public abstract void setDamage(@NotNull ItemMeta meta, @Nullable Integer damage);

    public abstract @Nullable Integer getDamage(@NotNull ItemMeta meta);

    public abstract @NotNull Map<String, Object> getCustomData(@NotNull ItemMeta meta);

    public abstract void setCustomData(@NotNull ItemMeta meta, @NotNull Map<String, Object> customData);

    protected abstract @NotNull Object toNBT(@NotNull Object java);

    protected abstract @NotNull Object fromNBT(@NotNull Object nbt);
}
