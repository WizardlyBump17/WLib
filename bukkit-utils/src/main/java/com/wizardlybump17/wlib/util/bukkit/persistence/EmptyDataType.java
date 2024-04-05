package com.wizardlybump17.wlib.util.bukkit.persistence;

import lombok.NonNull;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

public class EmptyDataType implements PersistentDataType<Byte, Object> {

    private static final @NonNull Object EMPTY_OBJECT = new Object();
    public static final @NonNull EmptyDataType INSTANCE = new EmptyDataType();

    @Override
    public @NonNull Class<Byte> getPrimitiveType() {
        return Byte.class;
    }

    @Override
    public @NonNull Class<Object> getComplexType() {
        return Object.class;
    }

    @Override
    public @NonNull Byte toPrimitive(@NonNull Object object, @NonNull PersistentDataAdapterContext context) {
        return (byte) 0;
    }

    @Override
    public @NonNull Object fromPrimitive(@NonNull Byte b, @NonNull PersistentDataAdapterContext context) {
        return EMPTY_OBJECT;
    }
}
