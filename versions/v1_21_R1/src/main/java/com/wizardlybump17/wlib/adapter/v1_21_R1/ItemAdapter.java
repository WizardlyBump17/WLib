package com.wizardlybump17.wlib.adapter.v1_21_R1;

import com.wizardlybump17.wlib.util.ReflectionUtil;
import lombok.NonNull;
import net.minecraft.nbt.*;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataContainer;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;

public class ItemAdapter extends com.wizardlybump17.wlib.adapter.ItemAdapter {

    public static final @NotNull Class<?> CRAFT_META_ITEM = ReflectionUtil.getClass("org.bukkit.craftbukkit.inventory.CraftMetaItem");
    public static final @NotNull Field CUSTOM_TAG = ReflectionUtil.getField("customTag", CRAFT_META_ITEM);

    @Override
    public void transferPersistentData(@NonNull PersistentDataContainer from, @NonNull PersistentDataContainer to) {
        Map<String, Tag> tags = ((CraftPersistentDataContainer) to).getRaw();
        tags.clear();
        tags.putAll(((CraftPersistentDataContainer) from).getTagsCloned());
    }

    @Override
    public void copyPersistentData(@NonNull PersistentDataContainer from, @NonNull PersistentDataContainer to) {
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

        for (String key : tag.getAllKeys()) {
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
            case Byte[] byteArray -> new ByteArrayTag(List.of(byteArray));
            case int[] intArray -> new IntArrayTag(intArray);
            case Integer[] intArray -> new IntArrayTag(List.of(intArray));
            case long[] longArray -> new LongArrayTag(longArray);
            case Long[] longArray -> new LongArrayTag(List.of(longArray));

            case Collection<?> collection -> {
                List<Tag> values = new ArrayList<>(collection.size());
                byte type = (byte) 0;
                for (Object object : collection) {
                    Tag tag = toNBT(object);
                    values.add(tag);
                    type = tag.getId();
                }
                yield new ListTag(values, type);
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
            case ByteTag tag -> tag.getAsByte();
            case ShortTag tag -> tag.getAsShort();
            case IntTag tag -> tag.getAsInt();
            case LongTag tag -> tag.getAsLong();
            case FloatTag tag -> tag.getAsFloat();
            case DoubleTag tag -> tag.getAsDouble();

            case StringTag tag -> tag.getAsString();

            case ByteArrayTag tag -> tag.getAsByteArray();
            case IntArrayTag tag -> tag.getAsIntArray();
            case LongArrayTag tag -> tag.getAsLongArray();

            case CollectionTag<?> tag -> {
                List<Object> list = new ArrayList<>(tag.size());
                for (Tag value : tag)
                    list.add(fromNBT(value));
                yield list;
            }

            case CompoundTag tag -> {
                Map<String, Object> map = new HashMap<>(tag.size());
                for (String key : tag.getAllKeys()) {
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