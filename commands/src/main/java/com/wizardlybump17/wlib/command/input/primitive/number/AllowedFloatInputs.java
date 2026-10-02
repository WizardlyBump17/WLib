package com.wizardlybump17.wlib.command.input.primitive.number;

import com.wizardlybump17.wlib.command.input.primitive.PrimitiveAllowedInputs;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface AllowedFloatInputs extends PrimitiveAllowedInputs<Float> {

    static @NotNull Range range(float from, float to) {
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

    static @NotNull Values values(@NotNull List<Float> values) {
        return new Values(values);
    }

    static @NotNull Value value(float value) {
        return new Value(value);
    }

    final class Range extends AllowedNumberInputs.Ranged<Float> implements AllowedFloatInputs {

        Range(float from, float to) {
            super(from, to);
            if (from >= to)
                throw new IllegalArgumentException("from must be less than to");
        }

        @Override
        public String toString() {
            return "AllowedFloatInputs$Range{" +
                    "from=" + from() +
                    ", to=" + to() +
                    "}";
        }
    }

    final class Unlimited extends AllowedNumberInputs.Unlimited<Float> implements AllowedFloatInputs {

        static final @NotNull AllowedFloatInputs.Unlimited INSTANCE = new AllowedFloatInputs.Unlimited();

        private Unlimited() {
        }

        @Override
        public String toString() {
            return "AllowedFloatInputs$Unlimited{}";
        }
    }

    final class Positive extends AllowedNumberInputs.Positive<Float> implements AllowedFloatInputs {

        static final @NotNull AllowedFloatInputs.Positive INSTANCE = new AllowedFloatInputs.Positive();

        private Positive() {
        }

        @Override
        public @NotNull Float from() {
            return 0.0F;
        }

        @Override
        public @NotNull Float to() {
            return Float.MAX_VALUE;
        }

        @Override
        public boolean isInRange(@NotNull Float input) {
            return input > -1;
        }

        @Override
        public String toString() {
            return "AllowedFloatInputs$Positive{}";
        }
    }

    final class Negative extends AllowedNumberInputs.Negative<Float> implements AllowedFloatInputs {

        static final @NotNull AllowedFloatInputs.Negative INSTANCE = new AllowedFloatInputs.Negative();

        private Negative() {
        }

        @Override
        public @NotNull Float from() {
            return -1.0F;
        }

        @Override
        public @NotNull Float to() {
            return Float.MIN_VALUE;
        }

        @Override
        public boolean isInRange(@NotNull Float input) {
            return input < 0;
        }

        @Override
        public String toString() {
            return "AllowedFloatInputs$Negative{}";
        }
    }

    final class Value extends AllowedNumberInputs.Value<Float> implements AllowedFloatInputs {

        Value(float value) {
            super(value);
        }

        @Override
        public String toString() {
            return "AllowedFloatInputs$Value{" +
                    "value=" + value() +
                    "}";
        }
    }

    final class Values extends AllowedNumberInputs.Values<Float> implements AllowedFloatInputs {

        Values(@NotNull List<Float> values) {
            super(values);
        }

        @Override
        public String toString() {
            return "AllowedFloatInputs$Values{"
                    + "values=" + allowedValues()
                    + '}';
        }
    }
}
