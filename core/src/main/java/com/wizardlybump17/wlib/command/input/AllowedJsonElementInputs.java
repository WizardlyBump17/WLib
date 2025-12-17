package com.wizardlybump17.wlib.command.input;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public interface AllowedJsonElementInputs extends AllowedInputs<JsonElement> {

    static @NotNull Value value(@NotNull JsonElement value) {
        return new Value(value);
    }

    static @NotNull Values values(@NotNull List<JsonElement> values) {
        return new Values(values);
    }

    static @NotNull Any anyNotNull() {
        return Any.NOT_NULL;
    }

    static @NotNull Any anyNullable() {
        return Any.NULLABLE;
    }

    final class Value implements AllowedJsonElementInputs, SingleValueInput<JsonElement> {

        private final @NotNull JsonElement value;

        Value(@NotNull JsonElement value) {
            this.value = value;
        }

        @Override
        public @NotNull JsonElement value() {
            return value;
        }

        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass())
                return false;
            Value value1 = (Value) object;
            return Objects.equals(value, value1.value);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }

        @Override
        public String toString() {
            return "AllowedJsonElementInputs$Value{" +
                    "value=" + value +
                    '}';
        }
    }

    final class Values implements AllowedJsonElementInputs, AllowedListInputs<JsonElement> {

        private final @NotNull List<JsonElement> values;

        private Values(@NotNull List<JsonElement> values) {
            this.values = values;
        }

        @Override
        public @NotNull List<JsonElement> allowedValues() {
            return values;
        }

        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass())
                return false;
            Values values1 = (Values) object;
            return Objects.equals(values, values1.values);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(values);
        }

        @Override
        public String toString() {
            return "AllowedJsonElementInputs$Values{" +
                    "values=" + values +
                    '}';
        }
    }

    final class Any implements AllowedJsonElementInputs {

        private static final @NotNull Any NOT_NULL = new Any(false);
        private static final @NotNull Any NULLABLE = new Any(true);

        private final boolean nullable;

        private Any(boolean nullable) {
            this.nullable = nullable;
        }

        @Override
        public boolean isAllowed(@Nullable JsonElement input) {
            return nullable || input != null;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass())
                return false;
            Any any = (Any) o;
            return nullable == any.nullable;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(nullable);
        }

        @Override
        public String toString() {
            return "AllowedJsonElementInputs$Any{" +
                    "nullable=" + nullable +
                    '}';
        }
    }
}
