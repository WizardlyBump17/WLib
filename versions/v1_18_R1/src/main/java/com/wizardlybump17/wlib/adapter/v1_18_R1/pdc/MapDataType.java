package com.wizardlybump17.wlib.adapter.v1_18_R1.pdc;

import com.wizardlybump17.wlib.adapter.v1_18_R1.ItemAdapter;
import com.wizardlybump17.wlib.adapter.v1_18_R1.NMSAdapter;
import lombok.RequiredArgsConstructor;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_18_R1.persistence.CraftPersistentDataContainer;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@RequiredArgsConstructor
public class MapDataType implements PersistentDataType<PersistentDataContainer, Map> {

    private final NMSAdapter adapter;

    @NotNull
    @Override
    public Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @NotNull
    @Override
    public Class<Map> getComplexType() {
        return Map.class;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public PersistentDataContainer toPrimitive(@NotNull Map map, @NotNull PersistentDataAdapterContext context) {
        PersistentDataContainer container = context.newPersistentDataContainer();
        map.forEach((key, value) -> container.set(new NamespacedKey(ItemAdapter.PLUGIN, key.toString()), (PersistentDataType<? extends Object, ? super Object>) adapter.getType(value), value));
        return container;
    }

    @NotNull
    @Override
    public Map fromPrimitive(@NotNull PersistentDataContainer container, @NotNull PersistentDataAdapterContext context) {
        return ((CraftPersistentDataContainer) container).getRaw();
    }
}
