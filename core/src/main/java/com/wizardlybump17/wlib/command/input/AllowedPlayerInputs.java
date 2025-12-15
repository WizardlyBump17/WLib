package com.wizardlybump17.wlib.command.input;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public interface AllowedPlayerInputs extends AllowedInputs<Player> {

    static @NotNull AllowedPlayerInputs.Value value(@NotNull Player value) {
        return new AllowedPlayerInputs.Value(value);
    }

    static @NotNull AllowedPlayerInputs.Values values(@NotNull List<Player> values) {
        return new AllowedPlayerInputs.Values(List.copyOf(values));
    }

    static @NotNull AllowedPlayerInputs.Values values(@NotNull Player @NotNull ... values) {
        return new AllowedPlayerInputs.Values(List.of(values));
    }

    static @NotNull AllowedPlayerInputs.Any anyNullable() {
        return AllowedPlayerInputs.Any.NULLABLE;
    }

    static @NotNull AllowedPlayerInputs.Any anyNotNull() {
        return AllowedPlayerInputs.Any.NOT_NULL;
    }

    final class Value implements AllowedPlayerInputs, SingleValueInput<Player> {

        private final @NotNull Player value;

        Value(@NotNull Player value) {
            this.value = value;
        }

        @Override
        public @NotNull Player value() {
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
            return "AllowedPlayerInputs$Value{" +
                    "value=" + value +
                    '}';
        }
    }

    final class Values implements AllowedPlayerInputs, AllowedListInputs<Player> {

        private final @NotNull List<Player> values;

        private Values(@NotNull List<Player> values) {
            this.values = values;
        }

        @Override
        public @NotNull List<Player> allowedValues() {
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
            return "AllowedPlayerInputs$Values{" +
                    "values=" + values +
                    '}';
        }
    }

    final class Any implements AllowedPlayerInputs {

        private static final @NotNull Any NULLABLE = new Any(true);
        private static final @NotNull Any NOT_NULL = new Any(false);

        private final boolean nullable;

        private Any(boolean nullable) {
            this.nullable = nullable;
        }

        @Override
        public boolean isAllowed(@Nullable Player input) {
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
            return "AllowedPlayerInputs$Any{" +
                    "nullable=" + nullable +
                    '}';
        }
    }
}
