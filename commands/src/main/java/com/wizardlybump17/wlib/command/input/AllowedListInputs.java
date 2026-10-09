package com.wizardlybump17.wlib.command.input;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public interface AllowedListInputs<T> extends AllowedInputs<T> {

    @NotNull List<T> allowedValues();

    @Override
    default boolean isAllowed(@Nullable T input) {
        if (input == null)
            return false;
        return allowedValues().contains(input);
    }

    final class Values<T> implements AllowedListInputs<T> {

        private final @NotNull List<T> values;

        Values(@NotNull List<T> values) {
            this.values = List.copyOf(values);
        }

        @Override
        public @NotNull List<T> allowedValues() {
            return values;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass())
                return false;
            Values<?> values1 = (Values<?>) o;
            return Objects.equals(values, values1.values);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(values);
        }

        @Override
        public String toString() {
            return "AllowedListInputs$Values{" +
                    "values=" + values +
                    '}';
        }
    }
}
