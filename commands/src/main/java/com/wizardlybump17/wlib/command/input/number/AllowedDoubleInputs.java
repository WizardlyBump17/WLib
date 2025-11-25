package com.wizardlybump17.wlib.command.input.number;

import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface AllowedDoubleInputs extends AllowedNumberInputs<Double> {

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

    final class Range extends Ranged<Double> implements AllowedDoubleInputs {

        Range(double from, double to) {
            super(from, to);
            if (from >= to)
                throw new IllegalArgumentException("from must be less than to");
        }

        @Override
        public @NotNull List<Double> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            double to = to();
            double from = from();

            if (to - from < 5) {
                Double[] doubles = new Double[(int) (to - from)];
                for (int i = 0; i < doubles.length; i++)
                    doubles[i] = to - from - i;
                return List.of(doubles);
            }

            double fourth = (to - from) / 4;
            return List.of(
                    from,
                    from + fourth,
                    from + fourth * 2,
                    from + fourth * 3,
                    to
            );
        }
    }

    final class Unlimited implements AllowedNumberInputs.Unlimited<Double>, AllowedDoubleInputs {

        static final @NotNull AllowedDoubleInputs.Unlimited INSTANCE = new AllowedDoubleInputs.Unlimited();

        private Unlimited() {
        }

        @Override
        public @NotNull List<Double> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            return List.of(
                    -100000.5,
                    -50000.5,
                    -3000.5,
                    -200.5,
                    -50.5,
                    0.0,
                    50.5,
                    200.5,
                    3000.5,
                    50000.5,
                    100000.5
            );
        }
    }

    final class Positive implements AllowedNumberInputs.Positive<Double>, AllowedDoubleInputs {

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
        public @NotNull List<Double> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            return List.of(
                    0.0,
                    50.5,
                    200.5,
                    3000.5,
                    50000.5,
                    100000.5
            );
        }
    }

    final class Negative implements AllowedNumberInputs.Negative<Double>, AllowedDoubleInputs {

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
        public @NotNull List<Double> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            return List.of(
                    -100000.5,
                    -50000.5,
                    -3000.5,
                    -200.5,
                    -50.5,
                    -1.0
            );
        }
    }

    final class Value extends AllowedNumberInputs.Value<Double> implements AllowedDoubleInputs {

        Value(double value) {
            super(value);
        }
    }

    final class Values extends AllowedNumberInputs.Values<Double> implements AllowedDoubleInputs {

        Values(@NotNull List<Double> values) {
            super(values);
        }
    }
}
