package com.wizardlybump17.wlib.adapter;

import com.wizardlybump17.wlib.adapter.util.LegacyStringUtil;
import com.wizardlybump17.wlib.adapter.util.StringUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class NMSAdapter {

    public static final String GLOW_TAG = "WLib-glowing";

    public abstract Object nbtToJava(Object nbt);

    public abstract Object javaToNbt(Object java);

    public abstract String getTargetVersion();

    public abstract ItemAdapter getItemAdapter(ItemStack item);

    public EntityAdapter getEntityAdapter(Entity entity) {
        if (EntityAdapter.ENTITY_CACHE.containsKey(entity.getUniqueId()))
            return EntityAdapter.ENTITY_CACHE.get(entity.getUniqueId());
        EntityAdapter adapter = newEntityAdapter(entity);
        EntityAdapter.ENTITY_CACHE.put(entity.getUniqueId(), adapter);
        return adapter;
    }

    public abstract EntityAdapter newEntityAdapter(Entity entity);

    public abstract ItemStack getFixedMaterial(WMaterial material);

    /**
     * @return the StringUtil instance for the current version
     */
    public StringUtil getStringUtil() {
        return new LegacyStringUtil();
    }
}
