package com.wizardlybump17.wlib.command.input;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface AllowedInputs<T> {

    boolean isAllowed(@Nullable T input);

    @SuppressWarnings("unchecked")
    static <T> @NotNull Any<T> anyNullable() {
        return (Any<T>) Any.NULLABLE;
    }

    @SuppressWarnings("unchecked")
    static <T> @NotNull Any<T> anyNotNull() {
        return (Any<T>) Any.NOT_NULL;
    }

    final class Any<T> implements AllowedInputs<T> {

        private static final @NotNull Any<Object> NULLABLE = new Any<>(true);
        private static final @NotNull Any<Object> NOT_NULL = new Any<>(false);

        private final boolean nullable;

        Any(boolean nullable) {
            this.nullable = nullable;
        }

        @Override
        public boolean isAllowed(@Nullable T input) {
            return nullable || input != null;
        }

        public boolean nullable() {
            return nullable;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass())
                return false;
            Any<?> any = (Any<?>) o;
            return nullable == any.nullable;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(nullable);
        }

        @Override
        public String toString() {
            return "AllowedInputs$Any{" +
                    "nullable=" + nullable +
                    '}';
        }
    }
}
