package com.wizardlybump17.wlib.adapter.v1_18_R1;

import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_18_R1.persistence.CraftPersistentDataContainer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

import static com.wizardlybump17.wlib.adapter.NMSAdapter.GLOW_TAG;

public class ItemAdapter extends com.wizardlybump17.wlib.adapter.ItemAdapter {

    private static final PersistentDataType<String, String> DEFAULT_TYPE = PersistentDataType.STRING;
    public static final Plugin PLUGIN = Bukkit.getPluginManager().getPlugin("WLib");

    protected ItemAdapter(ItemStack target, NMSAdapter mainAdapter) {
        super(target, CraftItemStack.asNMSCopy(target), mainAdapter);
    }

    @Override
    public void setNbtTag(String key, Object value) {
        meta.getPersistentDataContainer().set(new NamespacedKey(PLUGIN, key), (PersistentDataType<? extends Object, ? super Object>) mainAdapter.getType(value), value);
        target.setItemMeta(meta);
        meta = target.getItemMeta();
    }

    @Override
    public void setNbtTags(Map<String, Object> tags, boolean clearOld) {
        tags.forEach((key, value) ->
            meta.getPersistentDataContainer().set(new NamespacedKey(PLUGIN, key), (PersistentDataType<? extends Object, ? super Object>) mainAdapter.getType(value), value)
        );
        target.setItemMeta(meta);
        meta = target.getItemMeta();
    }

    @Override
    public Map<String, Object> getNbtTags() {
        Map<String, Object> map = (Map<String, Object>) mainAdapter.nbtToJava(((CraftPersistentDataContainer) meta.getPersistentDataContainer()).getRaw());
        return fixMap(map);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> fixMap(Map<String, Object> map) {
        Map<String, Object> result = new HashMap<>(map.size());
        map.forEach((key, value) -> {
            Object fixedValue = mainAdapter.nbtToJava(value);
            result.put(key.substring(5), fixedValue instanceof Map ? fixMap((Map<String, Object>) fixedValue) : fixedValue);
        });
        return result;
    }

    @Override
    public void removeNbtTag(String key) {
        meta.getPersistentDataContainer().remove(new NamespacedKey(PLUGIN, key));
    }

    @Override
    public boolean hasNbtTag(String key) {
        return meta.getPersistentDataContainer().has(new NamespacedKey(PLUGIN, key), DEFAULT_TYPE);
    }

    @Override
    public boolean hasNbtTag(String key, Object type) {
        return meta.getPersistentDataContainer().has(new NamespacedKey(PLUGIN, key), (PersistentDataType<?, ?>) type);
    }

    @Override
    public Object getNbtTag(String key) {
        return getNbtTag(key, DEFAULT_TYPE);
    }

    @Override
    public Object getNbtTag(String key, Object type) {
        return meta.getPersistentDataContainer().get(new NamespacedKey(PLUGIN, key), (PersistentDataType<?, ?>) type);
    }

    @Override
    public NBTTagCompound getMainTag() {
        throw new UnsupportedOperationException("Action not supported in 1.18");
    }

    @Override
    public void setMainTag(Object tag) {
        throw new UnsupportedOperationException("Action not supported in 1.18");
    }

    @Override
    public boolean isUnbreakable() {
        return meta.isUnbreakable();
    }

    @Override
    public NMSAdapter getMainAdapter() {
        return (NMSAdapter) mainAdapter;
    }

    @Override
    public void setUnbreakable(boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        target.setItemMeta(meta);
    }

    @Override
    public boolean hasGlow() {
        return hasNbtTag(GLOW_TAG);
    }

    @Override
    public void setGlow(boolean glow) {
        if (glow)
            setNbtTag(GLOW_TAG, "glow");
        else
            removeNbtTag(GLOW_TAG);
    }

    @Override
    public void setCustomModelData(Integer data) {
        meta.setCustomModelData(data);
        target.setItemMeta(meta);
    }

    @Override
    public Integer getCustomModelData() {
        return hasCustomModelData() ? meta.getCustomModelData() : null;
    }

    @Override
    public boolean hasCustomModelData() {
        return meta.hasCustomModelData();
    }
}