package com.wizardlybump17.wlib.command.input.primitive;

import com.wizardlybump17.wlib.command.input.AllowedInputs;
import com.wizardlybump17.wlib.command.input.AllowedListInputs;
import com.wizardlybump17.wlib.command.input.SingleValueInput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface AllowedCharacterInputs extends AllowedInputs<Character> {

    static @NotNull Value value(char value) {
        return new Value(value, false);
    }

    static @NotNull Value valueIgnoreCase(char value) {
        return new Value(value, true);
    }

    static @NotNull Values values(@NotNull List<Character> values) {
        values = List.copyOf(values);
        return new Values(values, values, false);
    }

    static @NotNull Values values(@NotNull Character @NotNull ... values) {
        List<Character> valuesList = List.of(values);
        return new Values(valuesList, valuesList, false);
    }

    static @NotNull Values valuesIgnoreCase(@NotNull List<Character> values) {
        return new Values(
                List.copyOf(values),
                values.stream()
                        .map(Character::toLowerCase)
                        .collect(Collectors.toList()),
                true
        );
    }

    static @NotNull Any anyNullable() {
        return Any.NULLABLE;
    }

    static @NotNull Any anyNotNull() {
        return Any.NOT_NULL;
    }

    final class Value implements AllowedCharacterInputs, SingleValueInput<Character> {

        private final char value;
        private final boolean ignoreCase;

        private Value(char value, boolean ignoreCase) {
            this.value = value;
            this.ignoreCase = ignoreCase;
        }

        @Override
        public boolean isAllowed(@Nullable Character input) {
            if (input == null)
                return false;
            return ignoreCase ? Character.toLowerCase(input) == Character.toLowerCase(value) : input == value;
        }

        @Override
        public @NotNull Character value() {
            return value;
        }

        public boolean ignoreCase() {
            return ignoreCase;
        }

        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass())
                return false;
            Value value1 = (Value) object;
            return value == value1.value && ignoreCase == value1.ignoreCase;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value, ignoreCase);
        }

        @Override
        public String toString() {
            return "Value{" +
                    "value=" + value +
                    ", ignoreCase=" + ignoreCase +
                    '}';
        }
    }

    final class Values implements AllowedCharacterInputs, AllowedListInputs<Character> {

        private final @NotNull List<Character> values;
        private final @NotNull List<Character> toCheck;
        private final boolean ignoreCase;

        private Values(@NotNull List<Character> values, @NotNull List<Character> toCheck, boolean ignoreCase) {
            this.values = values;
            this.toCheck = toCheck;
            this.ignoreCase = ignoreCase;
        }

        @Override
        public @NotNull List<Character> allowedValues() {
            return values;
        }

        public boolean isIgnoreCase() {
            return ignoreCase;
        }

        @Override
        public boolean isAllowed(@Nullable Character input) {
            if (input == null)
                return false;
            if (ignoreCase)
                return toCheck.contains(Character.toLowerCase(input));
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

    final class Any implements AllowedCharacterInputs {

        private static final @NotNull Any NULLABLE = new Any(true);
        private static final @NotNull Any NOT_NULL = new Any(false);

        private final boolean nullable;

        private Any(boolean nullable) {
            this.nullable = nullable;
        }

        public boolean nullable() {
            return nullable;
        }

        @Override
        public boolean isAllowed(@Nullable Character input) {
            return nullable || input != null;
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
