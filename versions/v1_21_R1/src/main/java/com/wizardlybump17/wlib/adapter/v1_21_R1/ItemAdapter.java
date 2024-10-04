package com.wizardlybump17.wlib.adapter.v1_21_R1;

import lombok.NonNull;
import net.minecraft.nbt.NBTBase;
import org.bukkit.craftbukkit.v1_21_R1.persistence.CraftPersistentDataContainer;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Map;

public class ItemAdapter extends com.wizardlybump17.wlib.adapter.ItemAdapter {

    @Override
    public void transferPersistentData(@NonNull PersistentDataContainer from, @NonNull PersistentDataContainer to) {
        Map<String, NBTBase> tags = ((CraftPersistentDataContainer) to).getRaw();
        tags.clear();
        tags.putAll(((CraftPersistentDataContainer) from).getRaw());
    }

    @Override
    public void copyPersistentData(@NonNull PersistentDataContainer from, @NonNull PersistentDataContainer to) {
        ((CraftPersistentDataContainer) to).getRaw().putAll(((CraftPersistentDataContainer) from).getRaw());
    }
}