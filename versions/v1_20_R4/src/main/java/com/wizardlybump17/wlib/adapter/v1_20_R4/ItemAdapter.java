package com.wizardlybump17.wlib.adapter.v1_20_R4;

import lombok.NonNull;
import net.minecraft.nbt.Tag;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataContainer;
import org.bukkit.persistence.PersistentDataContainer;

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
}