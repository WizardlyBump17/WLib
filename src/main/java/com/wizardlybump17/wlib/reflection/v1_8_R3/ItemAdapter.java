package com.wizardlybump17.wlib.reflection.v1_8_R3;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ItemAdapter extends com.wizardlybump17.wlib.reflection.ItemAdapter {

    protected ItemAdapter(ItemStack target, ReflectionAdapter mainAdapter) {
        super(target, mainAdapter);
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
            for (String s : new HashSet<>(compound.c()))
                compound.remove(s);
        for (Map.Entry<String, Object> entry : tags.entrySet())
            compound.set(entry.getKey(), getMainAdapter().javaToNbt(entry.getValue()));
        setMainTag(compound);
    }

    @Override
    public Map<String, Object> getNbtTags() {
        Map<String, Object> tags = new HashMap<>();
        NBTTagCompound compound = getMainTag();
        for (String s : compound.c())
            if (!IGNORED_TAGS.contains(s))
                tags.put(s, getMainAdapter().nbtToJava(compound.get(s)));
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
        NBTTagCompound compound = getMainTag();
        if (!compound.hasKey(key))
            return null;
        return mainAdapter.nbtToJava(compound.get(key));
    }

    @Override
    public net.minecraft.server.v1_8_R3.ItemStack toNmsStack() {
        return CraftItemStack.asNMSCopy(target);
    }

    @Override
    public NBTTagCompound getMainTag() {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = toNmsStack();
        if (!nmsStack.hasTag()) {
            nmsStack.setTag(new NBTTagCompound());
            target = CraftItemStack.asBukkitCopy(nmsStack);
        }
        return nmsStack.getTag();
    }

    @Override
    public void setMainTag(Object tag) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = toNmsStack();
        nmsStack.setTag((NBTTagCompound) tag);
        target = CraftItemStack.asBukkitCopy(nmsStack);
    }

    @Override
    public ReflectionAdapter getMainAdapter() {
        return (ReflectionAdapter) mainAdapter;
    }
}
