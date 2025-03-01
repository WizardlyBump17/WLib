package com.wizardlybump17.wlib.adapter.v1_21_R3;

import lombok.NonNull;
import net.minecraft.nbt.Tag;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataContainer;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ItemAdapter extends com.wizardlybump17.wlib.adapter.ItemAdapter {

    @Override
    public void transferPersistentData(@NonNull PersistentDataContainer from, @NonNull PersistentDataContainer to) {
        Map<String, Tag> tags = ((CraftPersistentDataContainer) to).getRaw();
        tags.clear();
        tags.putAll(((CraftPersistentDataContainer) from).getTagsCloned());
    }

    @Override
    public void copyPersistentData(@NonNull PersistentDataContainer from, @NonNull PersistentDataContainer to) {
        ((CraftPersistentDataContainer) to).getRaw().putAll(((CraftPersistentDataContainer) from).getTagsCloned());
    }

    @Override
    public void setDamage(@NotNull ItemMeta meta, @Nullable Integer damage) {
        if (!(meta instanceof Damageable damageable))
            return;

        if (damage == null)
            damageable.resetDamage();
        else
            damageable.setDamage(damage);
    }
}