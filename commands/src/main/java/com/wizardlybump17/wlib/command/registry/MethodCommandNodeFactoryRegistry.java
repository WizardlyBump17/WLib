package com.wizardlybump17.wlib.command.registry;

import com.wizardlybump17.wlib.command.extractor.method.factory.MethodCommandNodeFactory;
import com.wizardlybump17.wlib.command.extractor.method.factory.object.UUIDMethodCommandNodeFactory;
import com.wizardlybump17.wlib.command.extractor.method.factory.primitive.BooleanMethodCommandNodeFactory;
import com.wizardlybump17.wlib.command.extractor.method.factory.primitive.CharacterMethodCommandNodeFactory;
import com.wizardlybump17.wlib.command.extractor.method.factory.primitive.NumberMethodCommandNodeFactory;
import com.wizardlybump17.wlib.command.extractor.method.factory.string.StringMethodCommandNodeFactory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class MethodCommandNodeFactoryRegistry {

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

    public void addFactory(@NotNull MethodCommandNodeFactory factory) {
        addFactory(factory, factory.getSupportedTypes());
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
        addFactory(new BooleanMethodCommandNodeFactory());
        addFactory(new CharacterMethodCommandNodeFactory());
        addFactory(new NumberMethodCommandNodeFactory());
        addFactory(new StringMethodCommandNodeFactory());
        addFactory(new UUIDMethodCommandNodeFactory());
    }

    @ApiStatus.Internal
    public void unregisterDefaults() {
        removeFactory(
                boolean.class,
                Boolean.class,
                char.class,
                Character.class,
                byte.class,
                Byte.class,
                short.class,
                Short.class,
                int.class,
                Integer.class,
                long.class,
                Long.class,
                float.class,
                Float.class,
                double.class,
                Double.class,
                String.class,
                UUID.class
        );
    }
}
