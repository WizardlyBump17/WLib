package com.wizardlybump17.wlib.command.input.primitive.number;

import com.wizardlybump17.wlib.command.input.primitive.PrimitiveAllowedInputs;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface AllowedDoubleInputs extends PrimitiveAllowedInputs<Double> {

    static @NotNull Range range(double from, double to) {
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

    static @NotNull Values values(@NotNull List<Double> values) {
        return new Values(values);
    }

    static @NotNull Value value(double value) {
        return new Value(value);
    }

    final class Range extends AllowedNumberInputs.Ranged<Double> implements AllowedDoubleInputs {

        Range(double from, double to) {
            super(from, to);
            if (from >= to)
                throw new IllegalArgumentException("from must be less than to");
        }

        @Override
        public String toString() {
            return "AllowedByteInputs$Range{" +
                    "from=" + from() +
                    ", to=" + to() +
                    "}";
        }
    }

    final class Unlimited extends AllowedNumberInputs.Unlimited<Double> implements AllowedDoubleInputs {

        static final @NotNull AllowedDoubleInputs.Unlimited INSTANCE = new AllowedDoubleInputs.Unlimited();

        private Unlimited() {
        }

        @Override
        public String toString() {
            return "AllowedDoubleInputs$Unlimited{}";
        }
    }

    final class Positive extends AllowedNumberInputs.Positive<Double> implements AllowedDoubleInputs {

        static final @NotNull AllowedDoubleInputs.Positive INSTANCE = new AllowedDoubleInputs.Positive();

        private Positive() {
        }

        @Override
        public @NotNull Double from() {
            return 0.0;
        }

        @Override
        public @NotNull Double to() {
            return Double.MAX_VALUE;
        }

        @Override
        public boolean isInRange(@NotNull Double input) {
            return input > -1;
        }

        @Override
        public String toString() {
            return "AllowedDoubleInputs$Positive{}";
        }
    }

    final class Negative extends AllowedNumberInputs.Negative<Double> implements AllowedDoubleInputs {

        static final @NotNull AllowedDoubleInputs.Negative INSTANCE = new AllowedDoubleInputs.Negative();

        private Negative() {
        }

        @Override
        public @NotNull Double from() {
            return -1.0;
        }

        @Override
        public @NotNull Double to() {
            return Double.MIN_VALUE;
        }

        @Override
        public boolean isInRange(@NotNull Double input) {
            return input < 0;
        }

        @Override
        public String toString() {
            return "AllowedDoubleInputs$Negative{}";
        }
    }

    final class Value extends AllowedNumberInputs.Value<Double> implements AllowedDoubleInputs {

        Value(double value) {
            super(value);
        }

        @Override
        public String toString() {
            return "AllowedDoubleInputs$Value{" +
                    "value=" + value() +
                    "}";
        }
    }

    final class Values extends AllowedNumberInputs.Values<Double> implements AllowedDoubleInputs {

        Values(@NotNull List<Double> values) {
            super(values);
        }

        @Override
        public String toString() {
            return "AllowedDoubleInputs$Values{"
                    + "values=" + allowedValues()
                    + '}';
        }
    }
}
