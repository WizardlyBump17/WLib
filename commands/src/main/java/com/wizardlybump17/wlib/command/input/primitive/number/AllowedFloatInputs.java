package com.wizardlybump17.wlib.command.input.primitive.number;

import com.wizardlybump17.wlib.command.input.primitive.PrimitiveAllowedInputs;
import com.wizardlybump17.wlib.command.sender.CommandSender;
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
        public @NotNull List<Float> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            float to = to();
            float from = from();

            if (to - from < 5) {
                Float[] floats = new Float[(int) (to - from)];
                for (int i = 0; i < floats.length; i++)
                    floats[i] = to - from - i;
                return List.of(floats);
            }

            float fourth = (to - from) / 4;
            return List.of(
                    from,
                    from + fourth,
                    from + fourth * 2,
                    from + fourth * 3,
                    to
            );
        }
    }

    final class Unlimited extends AllowedNumberInputs.Unlimited<Float> implements AllowedFloatInputs {

        static final @NotNull AllowedFloatInputs.Unlimited INSTANCE = new AllowedFloatInputs.Unlimited();

        private Unlimited() {
        }

        @Override
        public @NotNull List<Float> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            return List.of(
                    -100000.5F,
                    -50000.5F,
                    -3000.5F,
                    -200.5F,
                    -50.5F,
                    0.0F,
                    50.5F,
                    200.5F,
                    3000.5F,
                    50000.5F,
                    100000.5F
            );
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
        public @NotNull List<Float> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            return List.of(
                    0.0F,
                    50.5F,
                    200.5F,
                    3000.5F,
                    50000.5F,
                    100000.5F
            );
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
        public @NotNull List<Float> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            return List.of(
                    -100000.5F,
                    -50000.5F,
                    -3000.5F,
                    -200.5F,
                    -50.5F,
                    -1.0F
            );
        }
    }

    final class Value extends AllowedNumberInputs.Value<Float> implements AllowedFloatInputs {

        Value(float value) {
            super(value);
        }
    }

    final class Values extends AllowedNumberInputs.Values<Float> implements AllowedFloatInputs {

        Values(@NotNull List<Float> values) {
            super(values);
        }
    }
}
