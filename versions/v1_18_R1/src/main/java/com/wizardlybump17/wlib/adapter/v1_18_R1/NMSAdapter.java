package com.wizardlybump17.wlib.adapter.v1_18_R1;

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
        NBTBase base = (NBTBase) nbt;

        if (base instanceof NBTTagByte)
            return ((NBTTagByte) base).h();
        if (base instanceof NBTTagByteArray)
            return ((NBTTagByteArray) base).d();
        if (base instanceof NBTTagShort)
            return ((NBTTagShort) base).g();
        if (base instanceof NBTTagInt)
            return ((NBTTagInt) base).f();
        if (base instanceof NBTTagIntArray)
            return ((NBTTagIntArray) base).f();
        if (base instanceof NBTTagLong)
            return ((NBTTagLong) base).e();
        if (base instanceof NBTTagFloat)
            return ((NBTTagFloat) base).j();
        if (base instanceof NBTTagDouble)
            return ((NBTTagDouble) base).i();

        if (base instanceof NBTTagString)
            return base.e_();

        if (base instanceof NBTTagList) {
            NBTTagList list = (NBTTagList) base;
            List<Object> javaList = new ArrayList<>();
            for (NBTBase nbtBase : list)
                javaList.add(nbtToJava(nbtBase));
            return javaList;
        }

        if (base instanceof NBTTagCompound) {
            NBTTagCompound compound = (NBTTagCompound) base;
            Map<String, Object> map = new LinkedHashMap<>();
            for (String key : compound.d())
                map.put(key, nbtToJava(compound.c(key)));
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
                compound.a(entry.getKey().toString(), javaToNbt(entry.getValue()));
            return compound;
        }

        return NBTTagString.a(java == null ? "null" : java.toString());
    }

    @Override
    public String getTargetVersion() {
        return "v1_18_R1";
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
        return new com.wizardlybump17.wlib.adapter.v1_18_R1.util.StringUtil();
    }
}
