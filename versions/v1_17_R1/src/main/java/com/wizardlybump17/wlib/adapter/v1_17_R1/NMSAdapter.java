package com.wizardlybump17.wlib.adapter.v1_17_R1;

import com.comphenix.protocol.ProtocolLibrary;
import com.wizardlybump17.wlib.adapter.WMaterial;
import com.wizardlybump17.wlib.adapter.util.StringUtil;
import net.minecraft.nbt.*;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NMSAdapter extends com.wizardlybump17.wlib.adapter.NMSAdapter {

    static {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketListener());
    }

    @Override
    public Object nbtToJava(Object nbt) {
        if (!(nbt instanceof NBTBase))
            return nbt;

        if (nbt instanceof NBTTagByte)
            return ((NBTTagByte) nbt).asByte();
        if (nbt instanceof NBTTagByteArray)
            return ((NBTTagByteArray) nbt).getBytes();
        if (nbt instanceof NBTTagShort)
            return ((NBTTagShort) nbt).asShort();
        if (nbt instanceof NBTTagInt)
            return ((NBTTagInt) nbt).asInt();
        if (nbt instanceof NBTTagIntArray)
            return ((NBTTagIntArray) nbt).getInts();
        if (nbt instanceof NBTTagLong)
            return ((NBTTagLong) nbt).asLong();
        if (nbt instanceof NBTTagFloat)
            return ((NBTTagFloat) nbt).asFloat();
        if (nbt instanceof NBTTagDouble)
            return ((NBTTagDouble) nbt).asDouble();
        if (nbt instanceof NBTTagString)
            return ((NBTTagString) nbt).asString();

        if (nbt instanceof NBTTagList) {
            NBTTagList list = (NBTTagList) nbt;
            List<Object> javaList = new ArrayList<>();
            for (NBTBase nbtBase : list)
                javaList.add(nbtToJava(nbtBase));
            return javaList;
        }

        if (nbt instanceof NBTTagCompound) {
            NBTTagCompound compound = (NBTTagCompound) nbt;
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
            return NBTTagByte.a((byte) java);
        if (java instanceof byte[])
            return new NBTTagByteArray((byte[]) java);
        if (java instanceof Short)
            return NBTTagShort.a((short) java);
        if (java instanceof Integer)
            return NBTTagInt.a((int) java);
        if (java instanceof int[])
            return new NBTTagIntArray((int[]) java);
        if (java instanceof Long)
            return NBTTagLong.a((long) java);
        if (java instanceof Float)
            return NBTTagFloat.a((float) java);
        if (java instanceof Double)
            return NBTTagDouble.a((double) java);
        if (java instanceof Boolean)
            return NBTTagByte.a((byte) ((boolean) java ? 1 : 0));

        if (java instanceof List) {
            List<?> list = (List<?>) java;
            NBTTagList tagList = new NBTTagList();
            for (Object o : list)
                tagList.add(javaToNbt(o));
            return tagList;
        }

        if (java instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) java;
            NBTTagCompound compound = new NBTTagCompound();
            for (Map.Entry<?, ?> entry : map.entrySet())
                compound.set(entry.getKey().toString(), javaToNbt(entry.getValue()));
            return compound;
        }

        return NBTTagString.a(java == null ? "null" : java.toString());
    }

    @Override
    public String getTargetVersion() {
        return "v1_17_R1";
    }

    @Override
    public ItemAdapter getItemAdapter(ItemStack item) {
        return new ItemAdapter(item, this);
    }

    @Override
    public EntityAdapter newEntityAdapter(Entity entity) {
        return new EntityAdapter(entity);
    }

    @Override
    public ItemStack getFixedMaterial(WMaterial material) {
        return material.getItemStack();
    }

    @Override
    public StringUtil getStringUtil() {
        return new com.wizardlybump17.wlib.adapter.v1_17_R1.util.StringUtil();
    }
}
