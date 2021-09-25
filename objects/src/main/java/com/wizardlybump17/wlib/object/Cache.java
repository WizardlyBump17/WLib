package com.wizardlybump17.wlib.object;

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

    protected final Map<K, V> cache = new HashMap<>();

    public void add(T t) {
        final Pair<K, V> apply = apply(t);
        cache.put(apply.getFirst(), apply.getSecond());
    }

    public void remove(K key) {
        cache.remove(key);
    }

    public boolean has(K key) {
        return cache.containsKey(key);
    }

    public Optional<V> get(K key) {
        return Optional.ofNullable(cache.get(key));
    }

    public V getOrInsert(K key, T def) {
        if (has(key))
            return get(key).orElse(null);
        final Pair<K, V> pair = apply(def);
        add(def);
        return pair.getSecond();
    }

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
    public abstract Pair<K, V> apply(T t);
}
