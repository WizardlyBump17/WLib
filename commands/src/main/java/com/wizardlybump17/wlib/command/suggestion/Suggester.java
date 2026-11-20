package com.wizardlybump17.wlib.command.suggestion;

import com.wizardlybump17.wlib.command.exception.SuggesterException;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public interface Suggester<T> {

    @NotNull List<T> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current, @NotNull CommandNode<?> currentNode) throws SuggesterException;

    default @NotNull String getStringRepresentation(@NotNull T value) {
        return value.toString();
    }

    default boolean needsEscape() {
        return false;
    }

    static <T> ValuesSuggester.@NotNull Values<T> values(@NotNull List<T> values, @Nullable Function<T, String> stringRepresentation, boolean needsEscape) {
        return new ValuesSuggester.Values<>(values, stringRepresentation, needsEscape);
    }

    static <T> ValuesSuggester.@NotNull Values<T> values(@NotNull List<T> values, @Nullable Function<T, String> stringRepresentation) {
        return Suggester.values(values, stringRepresentation, false);
    }
    
    static <T> ValuesSuggester.@NotNull Values<T> values(@NotNull List<T> values, boolean needsEscape) {
        return Suggester.values(values, null, needsEscape);
    }

    static <T> ValuesSuggester.@NotNull Values<T> values(@NotNull List<T> values) {
        return Suggester.values(values, null, false);
    }
}
