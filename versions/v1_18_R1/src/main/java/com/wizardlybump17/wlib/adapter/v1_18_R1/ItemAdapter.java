package com.wizardlybump17.wlib.adapter.v1_18_R1;

import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
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
        compound.a(key, getMainAdapter().javaToNbt(value));
        setMainTag(compound);
    }

    @Override
    public void setNbtTags(Map<String, Object> tags, boolean clearOld) {
        NBTTagCompound compound = getMainTag();
        if (clearOld)
            compound = new NBTTagCompound();
        for (Map.Entry<String, Object> entry : tags.entrySet())
            compound.a(entry.getKey(), getMainAdapter().javaToNbt(entry.getValue()));
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
        compound.r(key);
        setMainTag(compound);
    }

    @Override
    public boolean hasNbtTag(String key) {
        return getMainTag().e(key);
    }

    @Override
    public Object getNbtTag(String key) {
        return mainAdapter.nbtToJava(getMainTag().c(key));
    }

    @Override
    public NBTTagCompound getMainTag() {
        net.minecraft.world.item.ItemStack nmsStack = (net.minecraft.world.item.ItemStack) this.nmsStack;
        if (!nmsStack.r())
            setMainTag(new NBTTagCompound());
        return nmsStack.s();
    }

    @Override
    public void setMainTag(Object tag) {
        net.minecraft.world.item.ItemStack nmsStack = (net.minecraft.world.item.ItemStack) this.nmsStack;
        nmsStack.c((NBTTagCompound) tag);
        target = CraftItemStack.asBukkitCopy(nmsStack);
        meta = target.getItemMeta();
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