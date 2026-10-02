package com.wizardlybump17.wlib.bungee.util.collector;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class ComponentCollector implements Collector<BaseComponent, BaseComponent[], BaseComponent> {

    public static final @NotNull ComponentCollector COMMA = new ComponentCollector(new TextComponent(", "));
    public static final @NotNull ComponentCollector NEW_LINE = new ComponentCollector(new TextComponent("\n"));
    public static final @NotNull ComponentCollector EMPTY = new ComponentCollector(new TextComponent());

    private final @NotNull BaseComponent separator;

    public ComponentCollector(@NotNull BaseComponent separator) {
        this.separator = separator;
    }

    @Override
    public @NotNull Supplier<BaseComponent[]> supplier() {
        return () -> new BaseComponent[1];
    }

    @Override
    public @NotNull BiConsumer<BaseComponent[], BaseComponent> accumulator() {
        return (array, component) -> {
            if (array[0] == null)
                array[0] = component.duplicate();
            else {
                array[0] = array[0].duplicate();
                array[0].addExtra(separator);
                array[0].addExtra(component);
            }
        };
    }

    @Override
    public @NotNull BinaryOperator<BaseComponent[]> combiner() {
        return (left, right) -> {
            if (left[0] == null)
                left[0] = right[0].duplicate();
            else {
                left[0] = left[0].duplicate();
                left[0].addExtra(separator);
                left[0].addExtra(right[0].duplicate());
            }
            return left;
        };
    }

    @Override
    public @NotNull Function<BaseComponent[], BaseComponent> finisher() {
        return array -> Objects.requireNonNullElseGet(array[0], TextComponent::new);
    }

    @Override
    public @NotNull Set<Characteristics> characteristics() {
        return Set.of();
    }

    public @NotNull BaseComponent getSeparator() {
        return separator.duplicate();
    }
}
