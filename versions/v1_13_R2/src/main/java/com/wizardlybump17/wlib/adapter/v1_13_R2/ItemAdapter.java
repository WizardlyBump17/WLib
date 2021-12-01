package com.wizardlybump17.wlib.adapter.v1_13_R2;

import net.minecraft.server.v1_13_R2.NBTTagCompound;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

import static com.wizardlybump17.wlib.adapter.NMSAdapter.GLOW_TAG;

public class ItemAdapter extends com.wizardlybump17.wlib.adapter.ItemAdapter {

    protected ItemAdapter(ItemStack target, NMSAdapter mainAdapter) {
        super(target, CraftItemStack.asNMSCopy(target), mainAdapter);
    }

    @Override
    public void setNbtTag(String key, Object value) {
        NBTTagCompound compound = getMainTag();
        compound.set(key, getMainAdapter().javaToNbt(value));
        setMainTag(compound);
    }

    @Override
    public void setNbtTags(Map<String, Object> tags, boolean clearOld) {
        NBTTagCompound compound = getMainTag();
        if (clearOld)
            compound = new NBTTagCompound();
        for (Map.Entry<String, Object> entry : tags.entrySet())
            compound.set(entry.getKey(), getMainAdapter().javaToNbt(entry.getValue()));
        setMainTag(compound);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getNbtTags() {
        Map<String, Object> tags = (Map<String, Object>) getMainAdapter().nbtToJava(getMainTag());
        for (String tag : IGNORED_TAGS)
            tags.remove(tag);
        return tags;
    }

    @Override
    public void removeNbtTag(String key) {
        NBTTagCompound compound = getMainTag();
        compound.remove(key);
        setMainTag(compound);
    }

    @Override
    public boolean hasNbtTag(String key) {
        return getMainTag().hasKey(key);
    }

    @Override
    public Object getNbtTag(String key) {
        return mainAdapter.nbtToJava(getMainTag().get(key));
    }

    @Override
    public NBTTagCompound getMainTag() {
        net.minecraft.server.v1_13_R2.ItemStack nmsStack = (net.minecraft.server.v1_13_R2.ItemStack) this.nmsStack;
        if (!nmsStack.hasTag()) setMainTag(new NBTTagCompound());
        return nmsStack.getTag();
    }

    @Override
    public void setMainTag(Object tag) {
        net.minecraft.server.v1_13_R2.ItemStack nmsStack = (net.minecraft.server.v1_13_R2.ItemStack) this.nmsStack;
        nmsStack.setTag((NBTTagCompound) tag);
        target = CraftItemStack.asBukkitCopy(nmsStack);
        meta = target.getItemMeta();
    }

    @Override
    public NMSAdapter getMainAdapter() {
        return (NMSAdapter) mainAdapter;
    }

    @Override
    public boolean isUnbreakable() {
        return meta.isUnbreakable();
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
        if (data == null)
            removeNbtTag("CustomModelData");
        else
            setNbtTag("CustomModelData", data);
    }

    @Override
    public Integer getCustomModelData() {
        return (Integer) getNbtTags().get("CustomModelData");
    }

    @Override
    public boolean hasCustomModelData() {
        return getCustomModelData() != null;
    }
}
