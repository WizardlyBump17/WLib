package com.wizardlybump17.wlib.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class NumberUtil {

    public static @Nullable Number getNumber(@Nullable Object object) {
        return object instanceof Number number ? number : null;
    }
    
    public static @Nullable Byte getByte(@Nullable Object object) {
        return object instanceof Byte b ? b : null;
    }

    public static @Nullable Short getShort(@Nullable Object object) {
        return object instanceof Short s ? s : null;
    }

    public static @Nullable Integer getInteger(@Nullable Object object) {
        return object instanceof Integer i ? i : null;
    }

    public static @Nullable Long getLong(@Nullable Object object) {
        return object instanceof Long l ? l : null;
    }

    public static @Nullable Float getFloat(@Nullable Object object) {
        return object instanceof Float f ? f : null;
    }

    public static @Nullable Double getDouble(@Nullable Object object) {
        return object instanceof Double d ? d : null;
    }
}
