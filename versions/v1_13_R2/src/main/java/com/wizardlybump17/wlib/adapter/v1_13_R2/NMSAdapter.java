package com.wizardlybump17.wlib.adapter.v1_13_R2;

import com.wizardlybump17.wlib.adapter.WMaterial;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NMSAdapter extends com.wizardlybump17.wlib.adapter.NMSAdapter {

    @Override
    public Object nbtToJava(Object nbt) {
        if (!(nbt instanceof NBTBase))
            return nbt;
        NBTBase base = (NBTBase) nbt;

        if (base instanceof NBTTagByte)
            return ((NBTTagByte) base).asByte();
        if (base instanceof NBTTagByteArray)
            return ((NBTTagByteArray) base).c();
        if (base instanceof NBTTagShort)
            return ((NBTTagShort) base).asShort();
        if (base instanceof NBTTagInt)
            return ((NBTTagInt) base).asInt();
        if (base instanceof NBTTagIntArray)
            return ((NBTTagIntArray) base).d();
        if (base instanceof NBTTagLong)
            return ((NBTTagLong) base).asLong();
        if (base instanceof NBTTagFloat)
            return ((NBTTagFloat) base).asFloat();
        if (base instanceof NBTTagDouble)
            return ((NBTTagDouble) base).asDouble();

        if (base instanceof NBTTagString)
            return base.asString();

        if (base instanceof NBTTagList) {
            NBTTagList list = (NBTTagList) base;
            List<Object> javaList = new ArrayList<>();
            for (NBTBase nbtBase : list) javaList.add(nbtToJava(nbtBase));
            return javaList;
        }

        if (base instanceof NBTTagCompound) {
            NBTTagCompound compound = (NBTTagCompound) base;
            Map<String, Object> map = new LinkedHashMap<>();
            for (String key : compound.getKeys())
                map.put(key, nbtToJava(compound.get(key)));
            return map;
        }

        return null;
    }

    @Override
    public NBTBase javaToNbt(Object java) {
        if (java instanceof NBTBase)
            return (NBTBase) java;

        if (java instanceof Byte)
            return new NBTTagByte((byte) java);
        if (java instanceof byte[])
            return new NBTTagByteArray((byte[]) java);
        if (java instanceof Short)
            return new NBTTagShort((short) java);
        if (java instanceof Integer)
            return new NBTTagInt((int) java);
        if (java instanceof int[])
            return new NBTTagIntArray((int[]) java);
        if (java instanceof Long)
            return new NBTTagLong((long) java);
        if (java instanceof Float)
            return new NBTTagFloat((float) java);
        if (java instanceof Double)
            return new NBTTagDouble((double) java);

        if (java instanceof List) {
            List list = (List) java;
            NBTTagList tagList = new NBTTagList();
            for (Object o : list)
                tagList.add(javaToNbt(o));
            return tagList;
        }

        if (java instanceof Map) {
            Map map = (Map) java;
            NBTTagCompound compound = new NBTTagCompound();
            for (Object o : map.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                compound.set(entry.getKey().toString(), javaToNbt(entry.getValue()));
            }
            return compound;
        }

        return new NBTTagString(java == null ? "null" : java.toString());
    }

    @Override
    public String getTargetVersion() {
        return "v1_13_R2";
    }

    @Override
    public ItemAdapter getItemAdapter(ItemStack item) {
        return new ItemAdapter(item, this);
    }

    @Override
    public EntityAdapter getEntityAdapter(Entity entity) {
        return new EntityAdapter(entity);
    }

    @Override
    public ItemStack getFixedMaterial(WMaterial material) {
        return new ItemStack(material.getMaterial());
    }
}
