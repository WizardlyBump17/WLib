package com.wizardlybump17.wlib.command.input;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public interface AllowedOfflinePlayerInputs extends AllowedInputs<OfflinePlayer> {

    static @NotNull AllowedOfflinePlayerInputs.Value value(@NotNull OfflinePlayer value) {
        return new AllowedOfflinePlayerInputs.Value(value);
    }

    static @NotNull AllowedOfflinePlayerInputs.Values values(@NotNull List<OfflinePlayer> values) {
        return new AllowedOfflinePlayerInputs.Values(List.copyOf(values));
    }

    static @NotNull AllowedOfflinePlayerInputs.Values values(@NotNull OfflinePlayer @NotNull ... values) {
        return new AllowedOfflinePlayerInputs.Values(List.of(values));
    }

    static @NotNull AllowedOfflinePlayerInputs.Any anyNullable() {
        return AllowedOfflinePlayerInputs.Any.NULLABLE;
    }

    static @NotNull AllowedOfflinePlayerInputs.Any anyNotNull() {
        return AllowedOfflinePlayerInputs.Any.NOT_NULL;
    }

    final class Value implements AllowedOfflinePlayerInputs, SingleValueInput<OfflinePlayer> {

        private final @NotNull OfflinePlayer value;

        Value(@NotNull OfflinePlayer value) {
            this.value = value;
        }

        @Override
        public @NotNull OfflinePlayer value() {
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
            return "AllowedOfflinePlayerInputs$Value{" +
                    "value=" + value +
                    '}';
        }
    }

    final class Values implements AllowedOfflinePlayerInputs, AllowedListInputs<OfflinePlayer> {

        private final @NotNull List<OfflinePlayer> values;

        private Values(@NotNull List<OfflinePlayer> values) {
            this.values = values;
        }

        @Override
        public @NotNull List<OfflinePlayer> allowedValues() {
            return values;
        }

        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass())
                return false;
            AllowedOfflinePlayerInputs.Values values1 = (AllowedOfflinePlayerInputs.Values) object;
            return Objects.equals(values, values1.values);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(values);
        }

        @Override
        public String toString() {
            return "AllowedOfflinePlayerInputs$Values{" +
                    "values=" + values +
                    '}';
        }
    }

    final class Any implements AllowedOfflinePlayerInputs {

        private static final @NotNull AllowedOfflinePlayerInputs.Any NULLABLE = new AllowedOfflinePlayerInputs.Any(true);
        private static final @NotNull AllowedOfflinePlayerInputs.Any NOT_NULL = new AllowedOfflinePlayerInputs.Any(false);

        private final boolean nullable;

        private Any(boolean nullable) {
            this.nullable = nullable;
        }

        @Override
        public boolean isAllowed(@Nullable OfflinePlayer input) {
            return nullable || input != null;
        }

        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass())
                return false;
            AllowedOfflinePlayerInputs.Any any = (AllowedOfflinePlayerInputs.Any) object;
            return nullable == any.nullable;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(nullable);
        }

        @Override
        public String toString() {
            return "AllowedOfflinePlayerInputs$Any{" +
                    "nullable=" + nullable +
                    '}';
        }
    }
}
