package com.wizardlybump17.wlib.command.input.object;

import com.wizardlybump17.wlib.command.input.AllowedInputs;
import com.wizardlybump17.wlib.command.input.AllowedListInputs;
import com.wizardlybump17.wlib.command.input.SingleValueInput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public interface AllowedUUIDInputs extends AllowedInputs<UUID> {

    static @NotNull Value value(@NotNull UUID value) {
        return new Value(value);
    }

    static @NotNull Values values(@NotNull List<UUID> values) {
        return new Values(List.copyOf(values));
    }

    static @NotNull Values values(@NotNull UUID @NotNull ... values) {
        return new Values(List.of(values));
    }

    static @NotNull Any anyNullable() {
        return Any.NULLABLE;
    }

    static @NotNull Any anyNotNull() {
        return Any.NOT_NULL;
    }

    final class Value implements AllowedUUIDInputs, SingleValueInput<UUID> {

        private final @NotNull UUID value;

        Value(@NotNull UUID value) {
            this.value = value;
        }

        @Override
        public @NotNull UUID value() {
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
            return "AllowedUUIDInputs$Value{" +
                    "value=" + value +
                    '}';
        }
    }

    final class Values implements AllowedUUIDInputs, AllowedListInputs<UUID> {

        private final @NotNull List<UUID> values;

        private Values(@NotNull List<UUID> values) {
            this.values = values;
        }

        @Override
        public @NotNull List<UUID> allowedValues() {
            return values;
        }

        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass())
                return false;
            AllowedUUIDInputs.Values values1 = (AllowedUUIDInputs.Values) object;
            return Objects.equals(values, values1.values);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(values);
        }

        @Override
        public String toString() {
            return "AllowedUUIDInputs$Values{" +
                    "values=" + values +
                    '}';
        }
    }

    final class Any implements AllowedUUIDInputs {

        private static final @NotNull AllowedUUIDInputs.Any NULLABLE = new AllowedUUIDInputs.Any(true);
        private static final @NotNull AllowedUUIDInputs.Any NOT_NULL = new AllowedUUIDInputs.Any(false);

        private final boolean nullable;

        private Any(boolean nullable) {
            this.nullable = nullable;
        }

        @Override
        public boolean isAllowed(@Nullable UUID input) {
            return nullable || input != null;
        }

        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass())
                return false;
            AllowedUUIDInputs.Any any = (AllowedUUIDInputs.Any) object;
            return nullable == any.nullable;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(nullable);
        }

        @Override
        public String toString() {
            return "AllowedUUIDInputs$Any{" +
                    "nullable=" + nullable +
                    '}';
        }
    }
}
