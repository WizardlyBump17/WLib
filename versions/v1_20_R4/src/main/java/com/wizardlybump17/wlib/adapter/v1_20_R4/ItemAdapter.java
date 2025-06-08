package com.wizardlybump17.wlib.adapter.v1_20_R4;

import com.wizardlybump17.wlib.util.ReflectionUtil;
import lombok.NonNull;
import net.minecraft.nbt.*;
import org.bukkit.craftbukkit.v1_20_R4.persistence.CraftPersistentDataContainer;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;

public class ItemAdapter extends com.wizardlybump17.wlib.adapter.ItemAdapter {

    public static final @NotNull Class<?> CRAFT_META_ITEM = ReflectionUtil.getClass("org.bukkit.craftbukkit.inventory.v1_20_R4.CraftMetaItem");
    public static final @NotNull Field CUSTOM_TAG = ReflectionUtil.getField("customTag", CRAFT_META_ITEM);

    @Override
    public void transferPersistentData(@NonNull PersistentDataContainer from, @NonNull PersistentDataContainer to) {
        Map<String, NBTBase> tags = ((CraftPersistentDataContainer) to).getRaw();
        tags.clear();
        tags.putAll(((CraftPersistentDataContainer) from).getRaw());
    }

    @Override
    public void copyPersistentData(@NonNull PersistentDataContainer from, @NonNull PersistentDataContainer to) {
        ((CraftPersistentDataContainer) to).getRaw().putAll(((CraftPersistentDataContainer) from).getRaw());
    }

    @Override
    public @NotNull Map<String, Object> getCustomData(@NotNull ItemMeta meta) {
        Map<String, Object> result = new HashMap<>();

        NBTTagCompound tag = ReflectionUtil.getFieldValue(CUSTOM_TAG, meta);

        for (String key : tag.e()) {
            NBTBase data = tag.c(key);
            if (data != null)
                result.put(key, fromNBT(data));
        }

        return result;
    }

    @Override
    public void setCustomData(@NotNull ItemMeta meta, @NotNull Map<String, Object> customData) {
        NBTTagCompound tag = new NBTTagCompound();
        for (Map.Entry<String, Object> entry : customData.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            tag.a(key, toNBT(value));
        }

        ReflectionUtil.setFieldValue(CUSTOM_TAG, meta, tag);
    }

    @Override
    protected @NotNull NBTBase toNBT(@NotNull Object java) {
        return switch (java) {
            case Byte b -> NBTTagByte.a(b);
            case Short s -> NBTTagShort.a(s);
            case Integer i -> NBTTagInt.a(i);
            case Long l -> NBTTagLong.a(l);
            case Float f -> NBTTagFloat.a(f);
            case Double d -> NBTTagDouble.a(d);

            case Boolean b -> NBTTagByte.a(b);

            case String string -> NBTTagString.a(string);

            case byte[] byteArray -> new NBTTagByteArray(byteArray);
            case Byte[] byteArray -> new NBTTagByteArray(List.of(byteArray));
            case int[] intArray -> new NBTTagIntArray(intArray);
            case Integer[] intArray -> new NBTTagIntArray(List.of(intArray));
            case long[] longArray -> new NBTTagLongArray(longArray);
            case Long[] longArray -> new NBTTagLongArray(List.of(longArray));

            case Collection<?> collection -> {
                List<NBTBase> values = new ArrayList<>(collection.size());
                byte type = (byte) 0;
                for (Object object : collection) {
                    NBTBase tag = toNBT(object);
                    values.add(tag);
                    type = tag.b();
                }
                yield new NBTTagList(values, type);
            }

            case Map<?, ?> map -> {
                Map<String, NBTBase> values = new HashMap<>(map.size());
                map.forEach((key, value) -> values.put(String.valueOf(key), toNBT(value)));

                NBTTagCompound tag = new NBTTagCompound();
                values.forEach(tag::a);
                yield tag;
            }

            default -> throw new UnsupportedOperationException("Unsupported Java type: " + java);
        };
    }

    @Override
    protected @NotNull Object fromNBT(@NotNull Object nbt) {
        return switch (nbt) {
            case NBTTagByte tag -> tag.i();
            case NBTTagShort tag -> tag.h();
            case NBTTagInt tag -> tag.g();
            case NBTTagLong tag -> tag.f();
            case NBTTagFloat tag -> tag.k();
            case NBTTagDouble tag -> tag.j();

            case NBTTagString tag -> tag.s_();

            case NBTTagByteArray tag -> tag.e();
            case NBTTagIntArray tag -> tag.g();
            case NBTTagLongArray tag -> tag.g();

            case NBTList<?> tag -> {
                List<Object> list = new ArrayList<>(tag.size());
                for (NBTBase value : tag)
                    list.add(fromNBT(value));
                yield list;
            }

            case NBTTagCompound tag -> {
                Map<String, Object> map = new HashMap<>(tag.f());
                for (String key : tag.e()) {
                    NBTBase value = tag.c(key);
                    if (value != null)
                        map.put(key, fromNBT(value));
                }
                yield map;
            }

            default -> throw new UnsupportedOperationException("Unsupported NBT type: " + nbt);
        };
    }
}