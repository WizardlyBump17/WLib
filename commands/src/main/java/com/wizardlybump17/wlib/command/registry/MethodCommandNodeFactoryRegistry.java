package com.wizardlybump17.wlib.command.registry;

import com.wizardlybump17.wlib.command.extractor.method.factory.MethodCommandNodeFactory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class MethodCommandNodeFactoryRegistry {

    public static final @NotNull MethodCommandNodeFactoryRegistry INSTANCE = new MethodCommandNodeFactoryRegistry();

    private MethodCommandNodeFactoryRegistry() {
    }

    private final @NotNull Map<Class<?>, MethodCommandNodeFactory> factories = new HashMap<>();

    public void addFactory(@NotNull Class<?> clazz, @NotNull MethodCommandNodeFactory factory) {
        factories.put(clazz, factory);
    }

    public void addFactory(@NotNull MethodCommandNodeFactory factory, @NotNull Class<?> ... classes) {
        if (classes.length < 1)
            throw new IllegalArgumentException("The classes array must contain at least one element");
        for (Class<?> clazz : classes)
            addFactory(clazz, factory);
    }

    public @Nullable MethodCommandNodeFactory getFactory(@NotNull Class<?> clazz) {
        return factories.get(clazz);
    }

    public void removeFactory(@NotNull Class<?> clazz) {
        factories.remove(clazz);
    }

    public void removeFactory(@NotNull Class<?> ... classes) {
        if (classes.length < 1)
            throw new IllegalArgumentException("The classes array must contain at least one element");
        for (Class<?> clazz : classes)
            removeFactory(clazz);
    }

    public boolean hasFactory(@NotNull Class<?> clazz) {
        return factories.containsKey(clazz);
    }

    public @NotNull @Unmodifiable Map<Class<?>, MethodCommandNodeFactory> getFactories() {
        return Collections.unmodifiableMap(factories);
    }

    public void clear() {
        factories.clear();
    }

    @ApiStatus.Internal
    public void registerDefaults() {
    }

    @ApiStatus.Internal
    public void unregisterDefaults() {
    }
}
