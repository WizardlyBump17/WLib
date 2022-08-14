package com.wizardlybump17.wlib.adapter.v1_18_R1;

import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.craftbukkit.v1_18_R1.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.v1_18_R1.util.CraftNBTTagConfigSerializer;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Map;

public class ItemAdapter extends com.wizardlybump17.wlib.adapter.ItemAdapter {

    @SuppressWarnings("unchecked")
    @Override
    public Map<Object, Object> serializeContainer(PersistentDataContainer container) {
        return (Map<Object, Object>) CraftNBTTagConfigSerializer.serialize(((CraftPersistentDataContainer) container).toTagCompound());
    }

    @Override
    public PersistentDataContainer deserializeContainer(Map<Object, Object> map) {
        CraftPersistentDataContainer container = (CraftPersistentDataContainer) PERSISTENT_DATA_ADAPTER_CONTEXT.newPersistentDataContainer();
        container.putAll((NBTTagCompound) CraftNBTTagConfigSerializer.deserialize(map));
        return container;
    }

    @Override
    public void transferPersistentData(PersistentDataContainer from, PersistentDataContainer to) {
        CraftPersistentDataContainer craftTo = (CraftPersistentDataContainer) to;
        craftTo.getRaw().clear();
        craftTo.putAll(((CraftPersistentDataContainer) from).toTagCompound());
    }
}