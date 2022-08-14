package com.wizardlybump17.wlib.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapUtils {

    /**
     * Utility to check if the map contains another map
     * @param original the original map
     * @param toCheck the other map that contains the keys to be checked
     * @param <K> the map key type
     * @param <V> the value type
     * @return if the original map contains the other map
     */
    public static <K, V> boolean contains(Map<K, V> original, Map<K, V> toCheck) {
        return original.entrySet().containsAll(toCheck.entrySet());
    }

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
     * Apply the function to the map, converting it to a string
     * @param map the map target
     * @param function the function to be used to convert
     * @param <K> the map key type
     * @param <V> the map value type
     * @return the map converted to string
     */
    public static <K, V> String mapToString(Map<K, V> map, BiFunction<K, V, String> function) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<K, V> entry : map.entrySet())
            builder.append(function.apply(entry.getKey(), entry.getValue()));
        return builder.toString();
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
        return mapKeys(map, HashMap::new, function);
    }

    /**
     * Maps the map into a new Map provided by the supplier
     * @param map the map to be mapped
     * @param function the function
     * @param supplier the supplier to give us the new map
     * @param <K> the original key type
     * @param <V> the value type
     * @param <T> the new key type
     * @return the result map
     */
    public static <K, V, T> Map<T, V> mapKeys(Map<K, V> map, Supplier<Map<T, V>> supplier, Function<K, T> function) {
        Map<T, V> newMap = supplier.get();

        for (Map.Entry<K, V> entry : map.entrySet())
            newMap.put(function.apply(entry.getKey()), entry.getValue());

        return newMap;
    }

    /**
     * Creates a map using the given data. The data array must be a multiple of 2.<br>
     * The map type will be HashMap.<br>
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
     * Creates a map using the given data. The data array must be a multiple of 2.<br>
     * The map type is provided by the supplier.<br>
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
     * Removes all null values from the given map
     * @param map the map
     * @param <K> the map key type
     * @param <V> the map value type
     * @return the map without null values
     */
    public static <K, V> Map<K, V> removeNullValues(Map<K, V> map) {
        map.entrySet().removeIf(entry -> entry.getValue() == null);
        return map;
    }

    /**
     * Removes all empty values from the given map.<br>
     * It will check for {@link Map#isEmpty()} and {@link Collection#isEmpty()}
     * @param map the map
     * @param <K> the map key type
     * @param <V> the map value type
     * @return the map without empty values
     */
    public static <K, V> Map<K, V> removeEmptyValues(Map<K, V> map) {
        map.entrySet().removeIf(entry -> {
            if (entry.getValue() instanceof Map && ((Map<?, ?>) entry.getValue()).isEmpty())
                return true;
            return entry.getValue() instanceof Collection && ((Collection<?>) entry.getValue()).isEmpty();
        });
        return map;
    }

    /**
     * Sorts the given map by the given comparator based on the values.<br>
     * The returned value is a NEW {@link LinkedHashMap}
     * @param map the map to be sorted
     * @param comparator the comparator
     * @param <K> the key type
     * @param <V> the value type
     * @return the sorted map
     */
    public static <K, V> Map<K, V> sortByValues(Map<K, V> map, Comparator<V> comparator) {
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(comparator))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    /**
     * Returns a new reversed map based on the given map
     * @param map the map to be reversed
     * @param <K> the key type
     * @param <V> the value type
     * @return the reversed map
     */
    public static <K, V> Map<K, V> reversed(Map<K, V> map) {
        List<Map.Entry<K, V>> entries = new ArrayList<>(map.entrySet());
        Collections.reverse(entries);
        Map<K, V> reversed = new LinkedHashMap<>(entries.size());
        for (Map.Entry<K, V> entry : entries)
            reversed.put(entry.getKey(), entry.getValue());
        return reversed;
    }
}
