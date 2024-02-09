package com.wizardlybump17.wlib.util.builder;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * <p>A builder for creating {@link Map}s.</p>
 * @param <K> the key type
 * @param <V> the value type
 */
public class MapBuilder<K, V> {

    private final @NonNull Map<K, V> map;

    public MapBuilder(@NonNull Map<K, V> map) {
        this.map = map;
    }

    public MapBuilder(@NonNull Supplier<@NonNull Map<K, V>> supplier) {
        this(supplier.get());
    }

    public MapBuilder() {
        this(new HashMap<>());
    }

    public MapBuilder<K, V> put(@NonNull K key, @Nullable V value) {
        map.put(key, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public MapBuilder<K, V> put(@NonNull Object... data) {
        if (data.length % 2 != 0)
            throw new IllegalArgumentException("Data length must be even");

        for (int i = 0; i < data.length; i += 2) {
            K key = (K) data[i];
            V value = (V) data[i + 1];
            map.put(key, value);
        }

        return this;
    }

    public MapBuilder<K, V> putIfAbsent(@NonNull K key, @Nullable V value) {
        map.putIfAbsent(key, value);
        return this;
    }

    public MapBuilder<K, V> putIf(@NonNull K key, @Nullable V value, @NonNull Predicate<@Nullable V> condition) {
        if (condition.test(value))
            map.put(key, value);
        return this;
    }

    public MapBuilder<K, V> putIf(@NonNull K key, @Nullable V value, boolean condition) {
        if (condition)
            map.put(key, value);
        return this;
    }

    public MapBuilder<K, V> putIf(@NonNull K key, @NonNull Supplier<V> value, boolean condition) {
        if (condition)
            map.put(key, value.get());
        return this;
    }

    public MapBuilder<K, V> putIfNotEmpty(@NonNull K key, @Nullable V value) {
        return putIf(
                key,
                value,
                v -> {
                    if (v == null)
                        return false;

                    if (v instanceof Map<?,?> map)
                        return !map.isEmpty();

                    if (v instanceof Iterable<?> iterable)
                        return iterable.iterator().hasNext();

                    return true;
                }
        );
    }

    public MapBuilder<K, V> putIfNotNull(@NonNull K key, @Nullable V value) {
        return putIf(key, value, Objects::nonNull);
    }

    public MapBuilder<K, V> putAll(@NonNull Map<K, V> map) {
        this.map.putAll(map);
        return this;
    }

    public Map<K, V> build() {
        return map;
    }
}
