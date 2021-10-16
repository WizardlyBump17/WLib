package com.wizardlybump17.wlib.util;


import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

public class MapUtils {

    /**
     * Builds a map with the key and value. The Map type is HashMap
     * @param key the key
     * @param value the value
     * @param <K> the key type
     * @param <V> the value type
     * @return the map
     */
    public static <K, V> Map<K, V> mapOf(K key, V value) {
        return mapOf(HashMap::new, key, value);
    }

    /**
     * Builds a map with the key and value. Uses the map provided by the Supplier
     * @param supplier which map it should use
     * @param key the key
     * @param value the value
     * @param <K> the key type
     * @param <V> the value type
     * @return the map
     */
    public static <K, V> Map<K, V> mapOf(Supplier<Map<K, V>> supplier, K key, V value) {
        Map<K, V> map = supplier.get();
        map.put(key, value);
        return map;
    }

    /**
     * Maps the map into a new Map
     * @param map the map to be mapped
     * @param function the function
     * @param <K> the key type
     * @param <V> the original value type
     * @param <T> the new value type
     * @return the result map
     */
    public static <K, V, T> Map<K, T> mapValues(Map<K, V> map, Function<V, T> function) {
        Map<K, T> newMap = new HashMap<>(map.size());

        for (Map.Entry<K, V> entry : map.entrySet())
            newMap.put(entry.getKey(), function.apply(entry.getValue()));

        return newMap;
    }

    /**
     * Maps the map into a new Map
     * @param map the map to be mapped
     * @param function the function
     * @param <K> the original key type
     * @param <V> the value type
     * @param <T> the new key type
     * @return the result map
     */
    public static <K, V, T> Map<T, V> mapKeys(Map<K, V> map, Function<K, T> function) {
        Map<T, V> newMap = new HashMap<>(map.size());

        for (Map.Entry<K, V> entry : map.entrySet())
            newMap.put(function.apply(entry.getKey()), entry.getValue());

        return newMap;
    }

    /**
     * Creates a map using the given data. The data array must be a multiple of 2.
     * The map type will be HashMap
     * If the key or value is null, then it won't be stored
     * @param data the array
     * @param <K> the key type
     * @param <V> the value type
     * @return the map
     * @throws IllegalArgumentException if the array is not multiple of 2
     * @throws ClassCastException if one of the elements of the array is not of the type of the key or value
     */
    public static <K, V> Map<K, V> mapOf(Object... data) {
        return mapOf((Supplier<Map<K, V>>) HashMap::new, data);
    }

    /**
     * Creates a map using the given data. The data array must be a multiple of 2.
     * The map type is provided by the supplier
     * If the key or value is null, then it won't be stored
     * @param supplier which map it should use
     * @param data the array
     * @param <K> the key type
     * @param <V> the value type
     * @return the map
     * @throws IllegalArgumentException if the array is not multiple of 2
     * @throws ClassCastException if one of the elements of the array is not of the type of the key or value
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> mapOf(Supplier<Map<K, V>> supplier, Object... data) {
        if (data.length % 2 != 0)
            throw new IllegalArgumentException("invalid data for the map! They need to be multiple of 2");

        if (data.length == 0)
            return supplier.get();

        Map<K, V> map = supplier.get();
        for (int i = 0; i < data.length; i += 2)
            if (data[i] != null && data[i + 1] != null)
                map.put((K) data[i], (V) data[i + 1]);

        return map;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> mapOf(Supplier<Map<K, V>> supplier, BiPredicate<K, V> predicate, Object... data) {
        if (data.length % 2 != 0)
            throw new IllegalArgumentException("invalid data for the map! They need to be multiple of 2");

        if (data.length == 0)
            return supplier.get();

        Map<K, V> map = supplier.get();
        for (int i = 0; i < data.length; i += 2) {
            final K key = (K) data[i];
            final V value = (V) data[i + 1];

            if (predicate.test(key, value))
                map.put(key, value);
        }

        return map;
    }

    public static <K, V> Map<K, V> mapOf(BiPredicate<K, V> predicate, Object... data) {
        return mapOf((Supplier<Map<K,V>>) HashMap::new, predicate, data);
    }

    /**
     * "clones" the specified map. It tries to create a new instance of the map reflectively, exceptions may be thrown
     * @param map the original map
     * @param <K> the key type
     * @param <V> the value type
     * @param <T> the type of the map
     * @return the result map
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public static <K, V, T> T clone(Map<K, V> map) {
        try {
            final Map<K, V> result = map.getClass().newInstance();
            result.putAll(map);
            return (T) result;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return (T) new HashMap<K, V>();
        }
    }

    /**
     * @param <K>
     * @param <V>
     * @return
     * @deprecated use {{@link #mapOf(Object...)}}
     */
    @Deprecated
    public static <K, V> MapBuilder<K, V> builder() {
        return new MapBuilder<>();
    }

    /**
     * @deprecated Use {@link #mapOf(Object...)}
     * @param <K>
     * @param <V>
     */
    @Deprecated
    public static class MapBuilder<K, V> {

        private final Map<K, V> map = new HashMap<>();

        public <T> MapBuilder<K, V> putIf(RawPredicate predicate, K key, T t, Function<T, V> function) {
            if (predicate.test())
                return put(key, function.apply(t));
            return this;
        }

        public MapBuilder<K, V> putIf(RawPredicate predicate, K key, V value) {
            if (predicate.test())
                return put(key, value);
            return this;
        }

        public MapBuilder<K, V> put(K key, V value) {
            map.put(key, value);
            return this;
        }

        public MapBuilder<K, V> remove(K key) {
            map.remove(key);
            return this;
        }

        public boolean contains(K key) {
            return map.containsKey(key);
        }

        public Map<K, V> build() {
            return map;
        }
    }
}
