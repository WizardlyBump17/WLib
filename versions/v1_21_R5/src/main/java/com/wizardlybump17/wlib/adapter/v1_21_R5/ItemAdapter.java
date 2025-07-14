package com.wizardlybump17.wlib.adapter.v1_21_R5;

import com.wizardlybump17.wlib.util.ReflectionUtil;
import net.minecraft.nbt.*;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataContainer;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;

public class ItemAdapter extends com.wizardlybump17.wlib.adapter.ItemAdapter implements BaseAdapter {

    public static final @NotNull Class<?> CRAFT_META_ITEM = ReflectionUtil.getClass("org.bukkit.craftbukkit.inventory.CraftMetaItem");
    public static final @NotNull Field CUSTOM_TAG = ReflectionUtil.getField("customTag", CRAFT_META_ITEM);

    @Override
    public void transferPersistentData(@NotNull PersistentDataContainer from, @NotNull PersistentDataContainer to) {
        Map<String, Tag> tags = ((CraftPersistentDataContainer) to).getRaw();
        tags.clear();
        tags.putAll(((CraftPersistentDataContainer) from).getTagsCloned());
    }

    @Override
    public void copyPersistentData(@NotNull PersistentDataContainer from, @NotNull PersistentDataContainer to) {
        ((CraftPersistentDataContainer) to).getRaw().putAll(((CraftPersistentDataContainer) from).getTagsCloned());
    }

    @Override
    public void setDamage(@NotNull ItemMeta meta, @Nullable Integer damage) {
        if (!(meta instanceof Damageable damageable))
            return;

        if (damage == null)
            damageable.resetDamage();
        else
            damageable.setDamage(damage);
    }

    @Override
    public @Nullable Integer getDamage(@NotNull ItemMeta meta) {
        return meta instanceof Damageable damageable && damageable.hasDamage() ? damageable.getDamage() : null;
    }

    @Override
    public @NotNull Map<String, Object> getCustomData(@NotNull ItemMeta meta) {
        Map<String, Object> result = new HashMap<>();

        CompoundTag tag = ReflectionUtil.getFieldValue(CUSTOM_TAG, meta);
        if (tag == null)
            return result;

        for (String key : tag.keySet()) {
            Tag data = tag.get(key);
            if (data != null)
                result.put(key, fromNBT(data));
        }

        return result;
    }

    @Override
    public void setCustomData(@NotNull ItemMeta meta, @NotNull Map<String, Object> customData) {
        CompoundTag tag = new CompoundTag();
        for (Map.Entry<String, Object> entry : customData.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            tag.put(key, toNBT(value));
        }

        ReflectionUtil.setFieldValue(CUSTOM_TAG, meta, tag);
    }

    @Override
    protected @NotNull Tag toNBT(@NotNull Object java) {
        return switch (java) {
            case Byte b -> ByteTag.valueOf(b);
            case Short s -> ShortTag.valueOf(s);
            case Integer i -> IntTag.valueOf(i);
            case Long l -> LongTag.valueOf(l);
            case Float f -> FloatTag.valueOf(f);
            case Double d -> DoubleTag.valueOf(d);

            case Boolean b -> ByteTag.valueOf(b);

            case String string -> StringTag.valueOf(string);

            case byte[] byteArray -> new ByteArrayTag(byteArray);
            case Byte[] byteArray -> {
                byte[] array = new byte[byteArray.length];
                for (int i = 0; i < byteArray.length; i++)
                    array[i] = byteArray[i];
                yield new ByteArrayTag(array);
            }
            case int[] intArray -> new IntArrayTag(intArray);
            case Integer[] intArray -> {
                int[] array = new int[intArray.length];
                for (int i = 0; i < intArray.length; i++)
                    array[i] = intArray[i];
                yield new IntArrayTag(array);
            }
            case long[] longArray -> new LongArrayTag(longArray);
            case Long[] longArray -> {
                long[] array = new long[longArray.length];
                for (int i = 0; i < longArray.length; i++)
                    array[i] = longArray[i];
                yield new LongArrayTag(array);
            }

            case Collection<?> collection -> {
                List<Tag> values = new ArrayList<>(collection.size());
                for (Object object : collection) {
                    Tag tag = toNBT(object);
                    values.add(tag);
                }
                yield new ListTag(values);
            }

            case Map<?, ?> map -> {
                Map<String, Tag> values = new HashMap<>(map.size());
                map.forEach((key, value) -> values.put(String.valueOf(key), toNBT(value)));

                CompoundTag tag = new CompoundTag();
                values.forEach(tag::put);
                yield tag;
            }

            default -> throw new UnsupportedOperationException("Unsupported Java type: " + java);
        };
    }

    @Override
    protected @NotNull Object fromNBT(@NotNull Object nbt) {
        return switch (nbt) {
            case ByteTag tag -> tag.byteValue();
            case ShortTag tag -> tag.shortValue();
            case IntTag tag -> tag.intValue();
            case LongTag tag -> tag.longValue();
            case FloatTag tag -> tag.floatValue();
            case DoubleTag tag -> tag.doubleValue();

            case StringTag tag -> tag.value();

            case ByteArrayTag tag -> tag.getAsByteArray();
            case IntArrayTag tag -> tag.getAsIntArray();
            case LongArrayTag tag -> tag.getAsLongArray();

            case CollectionTag tag -> {
                List<Object> list = new ArrayList<>(tag.size());
                for (Tag value : tag)
                    list.add(fromNBT(value));
                yield list;
            }

            case CompoundTag tag -> {
                Map<String, Object> map = new HashMap<>(tag.size());
                for (String key : tag.keySet()) {
                    Tag value = tag.get(key);
                    if (value != null)
                        map.put(key, fromNBT(value));
                }
                yield map;
            }

            default -> throw new UnsupportedOperationException("Unsupported NBT type: " + nbt);
        };
    }
}