package com.wizardlybump17.wlib.util.bukkit.collector;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class ComponentCollector implements Collector<Component, Component[], Component> {

    public static final @NotNull ComponentCollector COMMA = new ComponentCollector(Component.text(", "));
    public static final @NotNull ComponentCollector NEW_LINE = new ComponentCollector(Component.text("\n"));
    public static final @NotNull ComponentCollector EMPTY = new ComponentCollector(Component.empty());

    private final @NotNull Component separator;

    public ComponentCollector(@NotNull Component separator) {
        this.separator = separator;
    }

    @Override
    public @NotNull Supplier<Component[]> supplier() {
        return () -> new Component[] {Component.empty()};
    }

    @Override
    public @NotNull BiConsumer<Component[], Component> accumulator() {
        return (array, component) -> array[0] = array[0].append(separator).append(component);
    }

    @Override
    public @NotNull BinaryOperator<Component[]> combiner() {
        return (left, right) -> {
            left[0] = left[0].append(separator).append(right[0]);
            return left;
        };
    }

    @Override
    public @NotNull Function<Component[], Component> finisher() {
        return array -> array[0];
    }

    @Override
    public @NotNull Set<Characteristics> characteristics() {
        return Set.of();
    }

    public @NotNull Component getSeparator() {
        return separator;
    }
}
