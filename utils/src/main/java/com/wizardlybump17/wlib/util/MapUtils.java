package com.wizardlybump17.wlib.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MapUtils {

    public static <K, V> Map<K, V> mapOf(K key, V value) {
        Map<K, V> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    public static <K, V> MapBuilder<K, V> builder() {
        return new MapBuilder<>();
    }

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
