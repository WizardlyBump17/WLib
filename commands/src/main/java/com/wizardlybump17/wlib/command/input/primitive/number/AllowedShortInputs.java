package com.wizardlybump17.wlib.command.input.primitive.number;

import com.wizardlybump17.wlib.command.input.primitive.PrimitiveAllowedInputs;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface AllowedShortInputs extends PrimitiveAllowedInputs<Short> {

    static @NotNull Range range(short from, short to) {
        return new Range(from, to);
    }

    static @NotNull Unlimited unlimited() {
        return Unlimited.INSTANCE;
    }

    static @NotNull Positive positive() {
        return Positive.INSTANCE;
    }

    static @NotNull Negative negative() {
        return Negative.INSTANCE;
    }

    static @NotNull Values values(@NotNull List<Short> values) {
        return new Values(values);
    }

    static @NotNull Value value(short value) {
        return new Value(value);
    }

    final class Range extends AllowedNumberInputs.Ranged<Short> implements AllowedShortInputs {

        Range(short from, short to) {
            super(from, to);
            if (from >= to)
                throw new IllegalArgumentException("from must be less than to");
        }

        @Override
        public String toString() {
            return "AllowedShortInputs$Range{" +
                    "from=" + from() +
                    ", to=" + to() +
                    "}";
        }
    }

    final class Unlimited extends AllowedNumberInputs.Unlimited<Short> implements AllowedShortInputs {

        static final @NotNull AllowedShortInputs.Unlimited INSTANCE = new AllowedShortInputs.Unlimited();

        private Unlimited() {
        }

        @Override
        public String toString() {
            return "AllowedShortInputs$Unlimited{}";
        }
    }

    final class Positive extends AllowedNumberInputs.Positive<Short> implements AllowedShortInputs {

        static final @NotNull AllowedShortInputs.Positive INSTANCE = new AllowedShortInputs.Positive();

        private Positive() {
        }

        @Override
        public @NotNull Short from() {
            return 0;
        }

        @Override
        public @NotNull Short to() {
            return Short.MAX_VALUE;
        }

        @Override
        public boolean isInRange(@NotNull Short input) {
            return input > -1;
        }

        @Override
        public String toString() {
            return "AllowedShortInputs$Positive{}";
        }
    }

    final class Negative extends AllowedNumberInputs.Negative<Short> implements AllowedShortInputs {

        static final @NotNull AllowedShortInputs.Negative INSTANCE = new AllowedShortInputs.Negative();

        private Negative() {
        }

        @Override
        public @NotNull Short from() {
            return -1;
        }

        @Override
        public @NotNull Short to() {
            return Short.MIN_VALUE;
        }

        @Override
        public boolean isInRange(@NotNull Short input) {
            return input < 0;
        }

        @Override
        public String toString() {
            return "AllowedShortInputs$Negative{}";
        }
    }

    final class Value extends AllowedNumberInputs.Value<Short> implements AllowedShortInputs {

        Value(short value) {
            super(value);
        }

        @Override
        public String toString() {
            return "AllowedShortInputs$Value{" +
                    "value=" + value() +
                    "}";
        }
    }

    final class Values extends AllowedNumberInputs.Values<Short> implements AllowedShortInputs {

        Values(@NotNull List<Short> values) {
            super(values);
        }

        @Override
        public String toString() {
            return "AllowedShortInputs$Values{"
                    + "values=" + allowedValues()
                    + '}';
        }
    }
}
