package com.wizardlybump17.wlib.adapter;

import lombok.Data;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Data
public abstract class ItemAdapter {

    protected static final String[] IGNORED_TAGS = {"display", "ench", "Enchantments", "HideFlags"};

    @NotNull
    protected ItemStack target;
    @NotNull
    protected ItemMeta meta;
    @NotNull
    protected Object nmsStack;
    @NotNull
    protected final NMSAdapter mainAdapter;

    public ItemAdapter(ItemStack itemStack, @NotNull Object nmsStack, @NotNull NMSAdapter mainAdapter) {
        target = itemStack;
        meta = itemStack.getItemMeta();
        this.nmsStack = nmsStack;
        this.mainAdapter = mainAdapter;
    }

    public abstract void setNbtTag(String key, Object value);
    public void setNbtTags(Map<String, Object> tags) {
        setNbtTags(tags, true);
    }
    public abstract void setNbtTags(Map<String, Object> tags, boolean clearOld);
    public abstract void removeNbtTag(String key);
    public abstract boolean hasNbtTag(String key);
    public abstract Object getNbtTag(String key);
    public abstract Map<String, Object> getNbtTags();

    /**
     * Sets the custom model data. If in older versions,
     * it will simply add the CustomModelData tag and won't have results in client-side
     * @param data
     */
    public abstract void setCustomModelData(int data);
    public abstract boolean hasCustomModelData();
    public abstract int getCustomModelData();

    public abstract Object getMainTag();
    public abstract void setMainTag(Object tag);

    public abstract boolean isUnbreakable();
    public abstract void setUnbreakable(boolean unbreakable);

    public abstract boolean hasGlow();
    public abstract void setGlow(boolean glow);
}
