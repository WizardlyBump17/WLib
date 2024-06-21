package com.wizardlybump17.wlib.util.bukkit;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@UtilityClass
public class ConfigUtil {

    /**
     * <p>
     *     Gets a value from the given {@link Map} by the given key.
     *     If the value is null, it will throw a {@link NullPointerException}.
     * </p>
     * @param key the key to get the value
     * @param map the map to get the value from
     * @return the value
     * @param <T> the type of the value
     */
    @SuppressWarnings("unchecked")
    public static <T> @NonNull T get(@NonNull String key, @NonNull Map<@NonNull String, @Nullable Object> map) {
        return (T) Objects.requireNonNull(map.get(key), "The key '" + key + "' is not present in the config!");
    }

    /**
     * <p>Gets a value from the given {@link Map} by the given key and map it using the given {@link Function}.</p>
     * @param key the key to get the value
     * @param map the map to get the value from
     * @param mapper the {@link Function} to map the value
     * @return the mapped value
     * @param <T> the type of the value
     */
    @SuppressWarnings("unchecked")
    public static <T, R> @NonNull R map(@NonNull String key, @NonNull Map<@NonNull String, @Nullable Object> map, @NonNull Function<T, R> mapper) {
        Object object = get(key, map);
        return Objects.requireNonNull(mapper.apply((T) object), "The value of the key '" + key + "' is null!");
    }

    /**
     * <p>
     *     Gets a value from the given {@link Map} by the given key.
     *     If the value is null, it will return the given default value.
     * </p>
     * @param key the key to get the value
     * @param map the map to get the value from
     * @param defaultValue the default value to return if the value is null
     * @return the value or the default value
     * @param <T> the type of the value
     */
    @Contract("_, _, !null -> !null")
    @SuppressWarnings("unchecked")
    public static <T> @Nullable T get(@NonNull String key, @NonNull Map<@NonNull String, @Nullable Object> map, @Nullable T defaultValue) {
        Object object = map.get(key);
        return object == null ? defaultValue : (T) object;
    }

    /**
     * <p>
     *     Gets a value from the given {@link Map} by the given key.
     *     If the value is null, it will use the {@link Supplier#get()} from the given {@link Supplier}.
     * </p>
     * @param key the key to get the value
     * @param map the map to get the value from
     * @param defaultValue the {@link Supplier} to return the default value if the value is null
     * @return the value or the default value
     * @param <T> the type of the value
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(@NonNull String key, @NonNull Map<@NonNull String, @Nullable Object> map, @NonNull Supplier<T> defaultValue) {
        Object object = map.get(key);
        return object == null ? defaultValue.get() : (T) object;
    }

    /**
     * <p>
     *     Gets a value from the given {@link Map} by the given key and maps it using the given {@link Function}.
     *     If the value is null, it will use the {@link Supplier#get()} from the given {@link Supplier}.
     * </p>
     * @param key the key to get the value
     * @param map the map to get the value from
     * @param supplier the {@link Supplier} to return the default value if the value is null
     * @param mapper the {@link Function} to map the value
     * @return the value or the default value
     * @param <T> the type of the value
     */
    @SuppressWarnings("unchecked")
    public static <T, R> R map(@NonNull String key, @NonNull Map<@NonNull String, @Nullable Object> map, @NonNull Supplier<T> supplier, @NonNull Function<T, R> mapper) {
        Object object = map.get(key);
        if (object == null)
            object = supplier.get();
        return object == null ? null: mapper.apply((T) object);
    }

    /**
     * <p>
     *     Gets a value from the given {@link Map} by the given key and maps it using the given {@link Function} mapper.
     *     If the value is {@code null}, it will use the {@link Supplier#get()} from the given default value {@link Supplier}.
     * </p>
     * @param key the key to get the value
     * @param map the {@link Map} to get the value from
     * @param defaultValue the {@link Supplier} to {@code return} the default value if the value in the {@link Map} is {@code null}
     * @param mapper the {@link Function} to {@link Map} the value
     * @return the value or the default value
     * @param <T> the type of the value
     * @param <R> the type of the mapped value
     */
    @SuppressWarnings("unchecked")
    public static <T, R> @Nullable R mapOrDefault(@NonNull String key, @NonNull Map<String, Object> map, @NonNull Supplier<R> defaultValue, @NonNull Function<T, R> mapper) {
        Object object = map.get(key);
        if (object == null)
            return defaultValue.get();
        return mapper.apply((T) object);
    }
}
