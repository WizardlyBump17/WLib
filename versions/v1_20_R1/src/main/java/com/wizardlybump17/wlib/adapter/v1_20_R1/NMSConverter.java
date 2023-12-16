package com.wizardlybump17.wlib.adapter.v1_20_R1;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import net.minecraft.nbt.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class NMSConverter {

    public static Object fromNBT(@NonNull NBTBase nbt) {
        return switch (nbt.b()) {
            case 0 -> null;
            case 1 -> ((NBTTagByte) nbt).i();
            case 2 -> ((NBTTagShort) nbt).c();
            case 3 -> ((NBTTagInt) nbt).g();
            case 4 -> ((NBTTagLong) nbt).f();
            case 5 -> ((NBTTagFloat) nbt).k();
            case 6 -> ((NBTTagDouble) nbt).j();
            case 7 -> ((NBTTagByteArray) nbt).e();
            case 8 -> nbt.m_();
            case 9 -> {
                NBTTagList nbtList = (NBTTagList) nbt;
                List<Object> list = new ArrayList<>(nbtList.size());
                for (NBTBase base : nbtList)
                    list.add(fromNBT(base));
                yield list;
            }
            case 10 -> {
                NBTTagCompound compound = (NBTTagCompound) nbt;
                Map<String, Object> map = new HashMap<>(compound.f());
                for (String key : compound.e())
                    map.put(key, fromNBT(compound.c(key)));
                yield map;
            }
            case 11 -> ((NBTTagIntArray) nbt).g();
            case 12 -> ((NBTTagLongArray) nbt).g();
            default -> null;
        };
    }

    public static NBTBase toNBT(@Nullable Object original) {
        if (original instanceof Byte b)
            return NBTTagByte.a(b);
        if (original instanceof Short s)
            return NBTTagShort.a(s);
        if (original instanceof Integer i)
            return NBTTagInt.a(i);
        if (original instanceof Long l)
            return NBTTagLong.a(l);
        if (original instanceof Float f)
            return NBTTagFloat.a(f);
        if (original instanceof Double d)
            return NBTTagDouble.a(d);

        if (original instanceof byte[] bytes)
            return new NBTTagByteArray(bytes);
        if (original instanceof int[] ints)
            return new NBTTagIntArray(ints);
        if (original instanceof long[] longs)
            return new NBTTagLongArray(longs);

        if (original instanceof String string)
            return NBTTagString.a(string);

        if (original instanceof List<?> list) {
            NBTTagList nbtList = new NBTTagList();
            for (Object object : list)
                nbtList.add(toNBT(object));
            return nbtList;
        }

        if (original instanceof Map<?, ?> map) {
            NBTTagCompound compound = new NBTTagCompound();
            for (Map.Entry<?, ?> entry : map.entrySet())
                compound.a(entry.getKey().toString(), toNBT(entry.getValue()));
            return compound;
        }

        return null;
    }
}
