package com.wizardlybump17.wlib.command.suggestion;

import com.wizardlybump17.wlib.command.exception.SuggesterException;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public interface ValuesSuggester<T> extends Suggester<T> {
    
    @NotNull List<T> values();

    final class Values<T> implements ValuesSuggester<T> {

        private final @NotNull List<T> values;
        private final @Nullable Function<T, String> stringRepresentationFunction;
        private final boolean needsEscape;

        Values(@NotNull List<T> values, @Nullable Function<T, String> stringRepresentationFunction, boolean needsEscape) {
            this.values = List.copyOf(values);
            this.stringRepresentationFunction = stringRepresentationFunction;
            this.needsEscape = needsEscape;
        }

        @Override
        public @NotNull @Unmodifiable List<T> values() {
            return values;
        }

        public @Nullable Function<T, String> stringRepresentation() {
            return stringRepresentationFunction;
        }

        @Override
        public boolean needsEscape() {
            return needsEscape;
        }

        @Override
        public @NotNull List<T> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current, @NotNull CommandNode<?> currentNode) throws SuggesterException {
            return values;
        }

        @Override
        public @NotNull String getStringRepresentation(@NotNull T value) {
            return stringRepresentationFunction == null ? ValuesSuggester.super.getStringRepresentation(value) : stringRepresentationFunction.apply(value);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass())
                return false;
            Values<?> values1 = (Values<?>) o;
            return Objects.equals(values, values1.values)
                    && Objects.equals(stringRepresentationFunction, values1.stringRepresentationFunction)
                    && needsEscape == values1.needsEscape;
        }

        @Override
        public int hashCode() {
            return Objects.hash(values, stringRepresentationFunction, needsEscape);
        }

        @Override
        public String toString() {
            return "ValuesSuggester$Values{" +
                    "values=" + values +
                    ", stringRepresentationFunction=" + stringRepresentationFunction +
                    ", needsEscape=" + needsEscape +
                    '}';
        }
    }
}
