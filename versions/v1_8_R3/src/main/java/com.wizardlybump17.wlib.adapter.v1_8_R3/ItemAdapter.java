package com.wizardlybump17.wlib.adapter.v1_8_R3;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

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

    @Override
    public Map<String, Object> getNbtTags() {
        Map<String, Object> map = (Map<String, Object>) getMainAdapter().nbtToJava(getMainTag());
        for (String ignoredTag : IGNORED_TAGS)
            map.remove(ignoredTag);
        return map;
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
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = (net.minecraft.server.v1_8_R3.ItemStack) this.nmsStack;
        return nmsStack.getTag() == null ? new NBTTagCompound() : nmsStack.getTag();
    }

    @Override
    public void setMainTag(Object tag) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = (net.minecraft.server.v1_8_R3.ItemStack) this.nmsStack;
        nmsStack.setTag((NBTTagCompound) tag);
        target = CraftItemStack.asBukkitCopy(nmsStack);
        meta = target.getItemMeta();
    }

    @Override
    public boolean isUnbreakable() {
        return meta.spigot().isUnbreakable();
    }

    @Override
    public NMSAdapter getMainAdapter() {
        return (NMSAdapter) mainAdapter;
    }

    @Override
    public void setUnbreakable(boolean unbreakable) {
        meta.spigot().setUnbreakable(unbreakable);
        target.setItemMeta(meta);
    }
}
