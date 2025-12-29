package com.wizardlybump17.wlib.command.suggestion;

import com.wizardlybump17.wlib.command.exception.SuggesterException;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.function.Function;

public interface ValuesSuggester<T> extends Suggester<T> {
    
    @NotNull List<T> values();

    final class Values<T> implements ValuesSuggester<T> {

        private final @NotNull List<T> values;
        private final @Nullable Function<T, String> stringRepresentationFunction;

        Values(@NotNull List<T> values, @NotNull Function<T, String> stringRepresentationFunction) {
            this.values = List.copyOf(values);
            this.stringRepresentationFunction = stringRepresentationFunction;
        }

        Values(@NotNull List<T> values) {
            this.values = List.copyOf(values);
            stringRepresentationFunction = null;
        }

        @Override
        public @NotNull @Unmodifiable List<T> values() {
            return values;
        }

        public @Nullable Function<T, String> stringRepresentation() {
            return stringRepresentationFunction;
        }

        @Override
        public @NotNull List<T> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current, @NotNull CommandNode<?> currentNode) throws SuggesterException {
            return values;
        }

        @Override
        public @NotNull String getStringRepresentation(@NotNull T value) {
            return stringRepresentationFunction == null ? ValuesSuggester.super.getStringRepresentation(value) : stringRepresentationFunction.apply(value);
        }
    }
}
