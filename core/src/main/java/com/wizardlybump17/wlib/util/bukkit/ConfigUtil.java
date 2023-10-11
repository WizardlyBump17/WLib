package com.wizardlybump17.wlib.util.bukkit;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

@UtilityClass
public class ConfigUtil {

    @SuppressWarnings("unchecked")
    public static <T> @NonNull T get(@NonNull String key, @NonNull Map<@NonNull String, @Nullable Object> map) {
        return (T) Objects.requireNonNull(map.get(key), "The key '" + key + "' is not present in the config!");
    }
}
