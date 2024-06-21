package com.wizardlybump17.wlib.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.*;
import java.util.function.*;
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
     * The map type is provided by the supplier.
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

    /**
     * Deep clones the given map.<br>
     * It will clone (if possible) the keys and values using {@link ObjectUtil#clone(Object)}
     * @param supplier the initial map supplier
     * @param map the map to be cloned
     * @return the cloned map
     * @param <K> the key type
     * @param <V> the value type
     */
    public static <K, V> Map<K, V> deepClone(Supplier<Map<K, V>> supplier, Map<K, V> map) {
        Map<K, V> newMap = supplier.get();

        for (Map.Entry<K, V> entry : map.entrySet())
            newMap.put(ObjectUtil.clone(entry.getKey()), ObjectUtil.clone(entry.getValue()));

        return newMap;
    }

    /**
     * Deep clones the given map.<br>
     * It will clone (if possible) the keys and values.<br>
     * It calls the {@link #deepClone(Supplier, Map)}
     * @param map the map to be cloned
     * @return the cloned map
     * @param <K> the key type
     * @param <V> the value type
     */
    public static <K, V> Map<K, V> deepClone(Map<K, V> map) {
        return deepClone((Supplier<Map<K, V>>) HashMap::new, map);
    }

    /**
     * <p>
     *     Creates a map using the given data. The data array must be a multiple of 3.<br>
     *     The data MUST be given like this:
     *     <ol>
     *         <li>Key</li>
     *         <li>Value</li>
     *         <li>{@link BooleanSupplier}</li>
     *     </ol>
     * </p>
     * @param supplier a {@link Supplier} with the initial {@link Map}
     * @param objects the data
     * @return the map
     * @param <K> the key type
     * @param <V> the value type
     */
    @SuppressWarnings("unchecked")
    public static <K, V> @NonNull Map<K, V> addIf(@NonNull Supplier<Map<K, V>> supplier, Object... objects) {
        if (objects.length == 0)
            return supplier.get();

        if (objects.length % 3 != 0)
            throw new IllegalArgumentException("Invalid array for the map! It needs to be multiple of 3");

        Map<K, V> map = supplier.get();
        for (int i = 0; i < objects.length; i += 3) {
            final K key = (K) objects[i];
            final V value = (V) objects[i + 1];
            final BooleanSupplier booleanSupplier = (BooleanSupplier) objects[i + 2];

            if (booleanSupplier.getAsBoolean())
                map.put(key, value);
        }

        return map;
    }

    /**
     * <p>
     *     Creates a map using the given data. The data array must be a multiple of 3.<br>
     *     The data MUST be given like this:
     *     <ol>
     *         <li>Key</li>
     *         <li>Value</li>
     *         <li>{@link BooleanSupplier}</li>
     *     </ol>
     * </p>
     * @param objects the data
     * @return the map
     * @param <K> the key type
     * @param <V> the value type
     */
    public static <K, V> @NonNull Map<K, V> addIf(@NonNull Object... objects) {
        return addIf((Supplier<Map<K, V>>) HashMap::new, objects);
    }

    /**
     * <p>Iterates the given {@link Map} and creates a new {@link LinkedHashMap}, converting its keys to a dot separated path.</p>
     * @param original the original {@link Map}
     * @return a new {@link LinkedHashMap} with the keys converted to a dot separated path
     */
    public static <V> @NonNull Map<String, V> dotKeys(@NonNull Map<String, Object> original) {
        Map<String, V> result = new LinkedHashMap<>();
        dotKeysRecursive(original, "", result);
        return result;
    }

    @SuppressWarnings("unchecked")
    private static <V> void dotKeysRecursive(@NonNull Map<String, Object> original, @NonNull String path, @NonNull Map<String, V> result) {
        for (Map.Entry<String, Object> entry : original.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            String newPath = path.isEmpty() ? key : path + "." + key;

            if (entry.getValue() instanceof Map<?, ?> map)
                dotKeysRecursive((Map<String, Object>) map, newPath, result);
            else
                result.put(newPath, (V) value);
        }
    }

    /**
     * <p>
     *     Creates a {@link Map} from the given {@link Collection}.
     *     The key will be the result of the {@link Function} and the value will be the element itself.
     * </p>
     * @param mapSupplier the {@link Map} supplier
     * @param collection the {@link Collection}
     * @param keyFunction the {@link Function} to get the key
     * @return the {@link Map} with the elements of the {@link Collection}
     * @param <K> the key type
     * @param <V> the value type
     */
    public static @NonNull <K, V> Map<K, V> collectionToMap(@NonNull Supplier<Map<K, V>> mapSupplier, @NonNull Collection<V> collection, @NonNull Function<V, K> keyFunction) {
        return collection.stream().collect(Collectors.toMap(keyFunction, Function.identity(), (key1, key2) -> key1, mapSupplier));
    }

    /**
     * <p>
     *     Creates a {@link Map} from the given {@link Collection}.
     *     The key will be the result of the {@link Function} and the value will be the element itself.
     * </p>
     * @param collection the {@link Collection}
     * @param keyFunction the {@link Function} to get the key
     * @return the {@link Map} with the elements of the {@link Collection}
     * @param <K> the key type
     * @param <V> the value type
     */
    public static @NonNull <K, V> Map<K, V> collectionToMap(@NonNull Collection<V> collection, @NonNull Function<V, K> keyFunction) {
        return collectionToMap(HashMap::new, collection, keyFunction);
    }

    /**
     * <p>
     *     Converts the given {@link Map} with dot separated keys to a new {@link Map} with the keys separated by the dots.
     *     <br>
     *     Example input | Output
     *     <ul>
     *         <li>{test.test1=1, test.test2=2, test.test3=3, test.test4.test=4} | {test={test1=1, test2=2, test3=3, test4={test=4}}}</li>
     *         <li>{test.test1=1, test.test2=2, test2=3, test3.test1=4} | {test={test1=t, test2=2}, test2=3, test3{test1=4}}</li>
     *     </ul>
     * </p>
     * @param mapSupplier the {@link Map} supplier that will be used to create all inner {@link Map}s
     * @param map the {@link Map} with the dot separated keys
     * @return the converted {@link Map}
     */
    @SuppressWarnings("unchecked")
    public static @NonNull Map<String, Object> dotToMap(@NonNull Supplier<Map<String, Object>> mapSupplier, @NonNull Map<String, String> map) {
        Map<String, Object> result = mapSupplier.get();
        map.forEach((key, value) -> {
            String[] keys = key.split("\\.");
            Map<String, Object> currentMap = result;
            for (int i = 0; i < keys.length - 1; i++) {
                String currentKey = keys[i];
                currentMap.computeIfAbsent(currentKey, $ -> mapSupplier.get());
                currentMap = (Map<String, Object>) currentMap.get(currentKey);
            }
            currentMap.put(keys[keys.length - 1], value);
        });
        return result;
    }

    /**
     * <p>
     *     Calls the {@link #dotToMap(Supplier, Map)} with a {@link HashMap} supplier.
     * </p>
     * @param map the {@link Map} with the dot separated keys
     * @return the converted {@link Map}
     */
    public static @NonNull Map<String, Object> dotToMap(@NonNull Map<String, String> map) {
        return dotToMap(HashMap::new, map);
    }
}
