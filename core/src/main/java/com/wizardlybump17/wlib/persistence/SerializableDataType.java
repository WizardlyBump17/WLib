package com.wizardlybump17.wlib.persistence;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class SerializableDataType implements PersistentDataType<byte[], Object> {

    public static final SerializableDataType INSTANCE = new SerializableDataType();

    @NotNull
    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @NotNull
    @Override
    public Class<Object> getComplexType() {
        return Object.class;
    }

    @Override
    public byte[] toPrimitive(@NotNull Object o, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream objectStream = new BukkitObjectOutputStream(byteStream)) {
            objectStream.writeObject(o);
            return byteStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    @NotNull
    @Override
    public Object fromPrimitive(@NotNull byte[] o, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(o);
             BukkitObjectInputStream inputStream = new BukkitObjectInputStream(byteStream)) {
            return inputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new Object();
        }
    }
}
