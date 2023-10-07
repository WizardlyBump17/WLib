package com.wizardlybump17.wlib.database.cache;

import com.wizardlybump17.wlib.database.controller.Controller;
import lombok.Data;
import lombok.NonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Data
public class ControllerCache {

    @NonNull
    public static final ControllerCache INSTANCE = new ControllerCache();

    @NonNull
    private final Set<Controller<?, ?, ?, ?>> cache = new HashSet<>();

    public void add(@NonNull Controller<?, ?, ?, ?> controller) {
        cache.add(controller);
    }

    public void remove(@NonNull Controller<?, ?, ?, ?> controller) {
        cache.remove(controller);
    }

    public void clear() {
        cache.clear();
    }

    public boolean contains(@NonNull Controller<?, ?, ?, ?> controller) {
        return cache.contains(controller);
    }

    public Set<Controller<?, ?, ?, ?>> getCache() {
        return Collections.unmodifiableSet(cache);
    }
}
