package com.wizardlybump17.wlib.object;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;


/**
 * Simple cache system.
 * The T is here because we don't want to do <pre>cache.add(key, value)</pre>.
 * @param <K> the key
 * @param <V> the value
 * @param <T> the class for {@link Cache#add(Object)}
 */
public abstract class Cache<K, V, T> {

    protected final Map<K, V> cache;

    protected Cache() {
        cache = getInitialMap();
    }

    public void add(@NotNull T t) {
        final Pair<K, V> apply = apply(t);
        cache.put(apply.getFirst(), apply.getSecond());
    }

    public void addAll(@NotNull Collection<T> collection) {
        for (T t : collection)
            add(t);
    }

    public void addAll(@NotNull Map<K, V> map) {
        cache.putAll(map);
    }

    /**
     * Removes the given key from the cache.
     * If the operation is successful, the returned value will be the VALUE that was removed
     * @param key the key to remove
     * @return the value that was removed
     */
    public Optional<V> remove(@Nullable K key) {
        if (key == null)
            return Optional.empty();
        return Optional.ofNullable(cache.remove(key));
    }

    public boolean has(@Nullable K key) {
        if (key == null)
            return false;
        return cache.containsKey(key);
    }

    @NotNull
    public Optional<V> get(@Nullable K key) {
        if (key == null)
            return Optional.empty();
        return Optional.ofNullable(cache.get(key));
    }

    /**
     * Attempts to get the value from the cache, if it's not present, it will be inserted and returned
     * @param key the key to get
     * @param def the default value
     * @return the value from the cache or the default value
     */
    @NotNull
    public V getOrInsert(@NotNull K key, @NonNull T def) {
        if (cache.containsKey(key))
            return cache.get(key);

        Pair<K, V> pair = apply(def);
        add(def);
        return pair.getSecond();
    }

    /**
     * Attempts to get the value from the cache, if it's not present, it will be inserted and returned
     * @param key the key to get
     * @param defaultValue the supplier for the default value
     * @return the value from the cache or from the supplier
     */
    @NotNull
    public V getOrInsert(@NotNull K key, @NotNull Supplier<T> defaultValue) {
        if (cache.containsKey(key))
            return cache.get(key);

        T def = defaultValue.get();
        if (def == null)
            throw new NullPointerException("The supplier returned a null value");

        Pair<K, V> pair = apply(def);
        add(def);
        return pair.getSecond();
    }

    @NotNull
    public Set<K> keySet() {
        return cache.keySet();
    }

    /**
     * @return the backing map of this cache
     */
    @NotNull
    public Map<K, V> getMap() {
        return cache;
    }

    @NotNull
    public Collection<V> getAll() {
        return cache.values();
    }

    public void clear() {
        cache.clear();
    }

    public int size() {
        return cache.size();
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }

    /**
     * This converts the given object to a valid pair to use in the map.
     * The first element of the Pair is the key, and the second is the value.
     * @param t the object
     * @return the pair to be used in the map
     */
    @NotNull
    public abstract Pair<K, V> apply(T t);

    /**
     * @return the initial map to be used
     */
    @NotNull
    protected Map<K, V> getInitialMap() {
        return new HashMap<>();
    }

    @NotNull
    public List<V> get(@NonNull Iterable<K> keys) {
        List<V> result = new ArrayList<>(size());
        for (K key : keys)
            get(key).ifPresent(result::add);
        return result;
    }
}
