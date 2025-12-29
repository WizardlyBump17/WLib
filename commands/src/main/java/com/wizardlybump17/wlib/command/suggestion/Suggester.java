package com.wizardlybump17.wlib.command.suggestion;

import com.wizardlybump17.wlib.command.exception.SuggesterException;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;

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

    static <T> ValuesSuggester.@NotNull Values<T> values(@NotNull List<T> values, @NotNull Function<T, String> stringRepresentation) {
        return new ValuesSuggester.Values<>(values, stringRepresentation);
    }
    
    static <T> ValuesSuggester.@NotNull Values<T> values(@NotNull List<T> values) {
        return new ValuesSuggester.Values<>(values);
    }
}
