package com.wizardlybump17.wlib.command.input.logical;

import com.wizardlybump17.wlib.command.input.AllowedInputs;
import com.wizardlybump17.wlib.command.input.SingleValueInput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface BooleanAllowedInputs extends AllowedInputs<Boolean> {

    static @NotNull True trueValue() {
        return True.INSTANCE;
    }

    static @NotNull False falseValue() {
        return False.INSTANCE;
    }

    static @NotNull Any anyNullable() {
        return Any.NULLABLE;
    }

    static @NotNull Any anyNotNull() {
        return Any.NOT_NULL;
    }

    final class True implements BooleanAllowedInputs, SingleValueInput<Boolean> {

        private static final @NotNull True INSTANCE = new True();

        private True() {
        }

        @Override
        public @NotNull Boolean value() {
            return true;
        }

        @Override
        public String toString() {
            return "True{}";
        }
    }

    final class False implements BooleanAllowedInputs, SingleValueInput<Boolean> {

        private static final @NotNull False INSTANCE = new False();

        private False() {
        }

        @Override
        public @NotNull Boolean value() {
            return false;
        }

        @Override
        public String toString() {
            return "False{}";
        }
    }

    final class Any implements BooleanAllowedInputs {

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
        public boolean isAllowed(@Nullable Boolean input) {
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
