package com.wizardlybump17.wlib.adapter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ItemAdapter {

    protected static final String[] IGNORED_TAGS = {"display", "ench", "Enchantments", "HideFlags"};

    protected ItemStack target;
    protected Object nmsStack;
    protected final NMSAdapter mainAdapter;

    public abstract void setNbtTag(String key, Object value);
    public void setNbtTags(Map<String, Object> tags) {
        setNbtTags(tags, true);
    }
    public abstract void setNbtTags(Map<String, Object> tags, boolean clearOld);
    public abstract void removeNbtTag(String key);
    public abstract boolean hasNbtTag(String key);
    public abstract Object getNbtTag(String key);
    public abstract Map<String, Object> getNbtTags();

    public abstract Object getMainTag();
    public abstract void setMainTag(Object tag);

    public abstract boolean isUnbreakable();
    public abstract void setUnbreakable(boolean unbreakable);
}
