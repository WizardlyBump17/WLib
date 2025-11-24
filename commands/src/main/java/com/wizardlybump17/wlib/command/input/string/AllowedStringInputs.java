package com.wizardlybump17.wlib.command.input.string;

import com.wizardlybump17.wlib.command.input.AllowedInputs;
import com.wizardlybump17.wlib.command.input.AllowedListInputs;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Objects;

public interface AllowedStringInputs extends AllowedInputs<String> {

    static @NotNull Values valuesIgnoreCase(@NotNull List<String> values) {
        return new Values(values, true);
    }

    static @NotNull Values values(@NotNull List<String> values) {
        return new Values(values, false);
    }

    static @NotNull Any anyNullable() {
        return Any.NULLABLE;
    }

    static @NotNull Any anyNotNull() {
        return Any.NOT_NULL;
    }

    final class Values implements AllowedStringInputs, AllowedListInputs<String> {

        private final @NotNull List<String> values;
        private final @NotNull List<String> toCheck;
        private final boolean ignoreCase;

        private Values(@NotNull List<String> values, boolean ignoreCase) {
            this.values = List.copyOf(values);
            this.ignoreCase = ignoreCase;
            if (ignoreCase)
                toCheck = values.stream().map(String::toLowerCase).toList();
            else
                toCheck = this.values;
        }

        @Override
        public @NotNull @Unmodifiable List<String> getAllowedValues() {
            return values;
        }

        @Override
        public @NotNull List<String> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            return values;
        }

        @Override
        public boolean isAllowed(@Nullable String input) {
            if (input == null)
                return false;
            if (ignoreCase)
                return toCheck.contains(input.toLowerCase());
            return toCheck.contains(input);
        }

        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass())
                return false;
            Values values1 = (Values) object;
            return ignoreCase == values1.ignoreCase && Objects.equals(values, values1.values) && Objects.equals(toCheck, values1.toCheck);
        }

        @Override
        public int hashCode() {
            return Objects.hash(values, toCheck, ignoreCase);
        }

        @Override
        public String toString() {
            return "Values{" +
                    "values=" + values +
                    ", ignoreCase=" + ignoreCase +
                    '}';
        }
    }

    final class Any implements AllowedStringInputs {

        private static final @NotNull Any NULLABLE = new Any(true);
        private static final @NotNull Any NOT_NULL = new Any(false);

        private final boolean nullable;

        private Any(boolean nullable) {
            this.nullable = nullable;
        }

        @Override
        public boolean isAllowed(@Nullable String input) {
            return nullable || input != null;
        }

        public boolean isNullable() {
            return nullable;
        }

        @Override
        public @NotNull List<String> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            return List.of(current);
        }

        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass())
                return false;
            Any any = (Any) object;
            return nullable == any.nullable;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(nullable);
        }

        @Override
        public String toString() {
            return "Any{" +
                    "nullable=" + nullable +
                    '}';
        }
    }
}
