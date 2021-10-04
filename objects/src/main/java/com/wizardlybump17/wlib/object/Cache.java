package com.wizardlybump17.wlib.object;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * Simple cache system.
 * The T is here because we don't want to do <pre>cache.add(key, value)</pre>.
 * @param <K> the key
 * @param <V> the value
 * @param <T> the class for {@link Cache#add(Object)}
 */
public abstract class Cache<K, V, T> {

    protected final Map<K, V> cache;

    public Cache() {
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

    public void remove(@NotNull K key) {
        cache.remove(key);
    }

    public boolean has(@NotNull K key) {
        return cache.containsKey(key);
    }

    @NotNull
    public Optional<V> get(@NotNull K key) {
        return Optional.ofNullable(cache.get(key));
    }

    @Nullable
    public V getOrInsert(@NotNull K key, @NotNull T def) {
        if (has(key))
            return get(key).orElse(null);
        final Pair<K, V> pair = apply(def);
        add(def);
        return pair.getSecond();
    }

    @NotNull
    public Collection<V> getAll() {
        return cache.values();
    }

    public void clear() {
        cache.clear();
    }

    /**
     * This converts the given object to a valid pair to use in the map
     * @param t the object
     * @return the pair
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
}
