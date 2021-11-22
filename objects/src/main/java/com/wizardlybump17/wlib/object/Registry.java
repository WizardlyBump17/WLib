package com.wizardlybump17.wlib.object;

import java.util.HashMap;
import java.util.Map;

public abstract class Registry<K, V> {

    private final Map<K, V> map = new HashMap<>();

    public V get(K key) {
        return map.get(key);
    }

    public void put(K key, V value) {
        map.put(key, value);
    }

    public boolean has(K key) {
        return map.containsKey(key);
    }

    public void remove(K key) {
        map.remove(key);
    }
}
