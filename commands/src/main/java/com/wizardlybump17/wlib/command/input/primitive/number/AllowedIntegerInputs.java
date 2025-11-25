package com.wizardlybump17.wlib.command.input.primitive.number;

import com.wizardlybump17.wlib.command.input.primitive.PrimitiveAllowedInputs;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.IntStream;

public interface AllowedIntegerInputs extends PrimitiveAllowedInputs<Integer> {

    static @NotNull Range range(int from, int to) {
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

    static @NotNull Values values(@NotNull List<Integer> values) {
        return new Values(values);
    }

    static @NotNull Value value(int value) {
        return new Value(value);
    }

    final class Range extends AllowedNumberInputs.Ranged<Integer> implements AllowedIntegerInputs {

        Range(int from, int to) {
            super(from, to);
            if (from >= to)
                throw new IllegalArgumentException("from must be less than to");
        }

        @Override
        public @NotNull List<Integer> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            int to = to();
            int from = from();

            if (to - from < 5)
                return IntStream.rangeClosed(from, to).boxed().toList();

            int fourth = (to - from) / 4;
            return List.of(from, from + fourth, from + fourth * 2, from + fourth * 3, to);
        }
    }

    final class Unlimited extends AllowedNumberInputs.Unlimited<Integer> implements AllowedIntegerInputs{

        static final @NotNull AllowedIntegerInputs.Unlimited INSTANCE = new AllowedIntegerInputs.Unlimited();

        private Unlimited() {
        }

        @Override
        public @NotNull List<Integer> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            return List.of(
                    -100000,
                    -50000,
                    -3000,
                    -200,
                    -50,
                    0,
                    50,
                    200,
                    3000,
                    50000,
                    100000
            );
        }
    }

    final class Positive extends AllowedNumberInputs.Positive<Integer> implements AllowedIntegerInputs {

        static final @NotNull AllowedIntegerInputs.Positive INSTANCE = new AllowedIntegerInputs.Positive();

        private Positive() {
        }

        @Override
        public @NotNull Integer from() {
            return 0;
        }

        @Override
        public @NotNull Integer to() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isInRange(@NotNull Integer input) {
            return input > -1;
        }

        @Override
        public @NotNull List<Integer> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            return List.of(
                    0,
                    50,
                    200,
                    3000,
                    50000,
                    100000
            );
        }
    }

    final class Negative extends AllowedNumberInputs.Negative<Integer> implements AllowedIntegerInputs {

        static final @NotNull AllowedIntegerInputs.Negative INSTANCE = new AllowedIntegerInputs.Negative();

        private Negative() {
        }

        @Override
        public @NotNull Integer from() {
            return -1;
        }

        @Override
        public @NotNull Integer to() {
            return Integer.MIN_VALUE;
        }

        @Override
        public boolean isInRange(@NotNull Integer input) {
            return input < 0;
        }

        @Override
        public @NotNull List<Integer> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            return List.of(
                    -100000,
                    -50000,
                    -3000,
                    -200,
                    -50,
                    -1
            );
        }
    }

    final class Value extends AllowedNumberInputs.Value<Integer> implements AllowedIntegerInputs {

        Value(@NotNull Integer value) {
            super(value);
        }
    }

    final class Values extends AllowedNumberInputs.Values<Integer> implements AllowedIntegerInputs {

        Values(@NotNull List<Integer> values) {
            super(values);
        }
    }
}
