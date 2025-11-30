package com.wizardlybump17.wlib.command.input.primitive.number;

import com.wizardlybump17.wlib.command.input.primitive.PrimitiveAllowedInputs;
import com.wizardlybump17.wlib.command.sender.CommandSender;
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
        public @NotNull List<Short> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            short to = to();
            short from = from();

            if (to - from < 5) {
                Short[] shorts = new Short[to - from];
                for (int i = 0; i < shorts.length; i++)
                    shorts[i] = (short) (to - from - i);
                return List.of(shorts);
            }

            short fourth = (short) ((to - from) / 4);
            return List.of(
                    from,
                    (short) (from + fourth),
                    (short) (from + fourth * 2),
                    (short) (from + fourth * 3),
                    to
            );
        }
    }

    final class Unlimited extends AllowedNumberInputs.Unlimited<Short> implements AllowedShortInputs {

        static final @NotNull AllowedShortInputs.Unlimited INSTANCE = new AllowedShortInputs.Unlimited();

        private Unlimited() {
        }

        @Override
        public @NotNull List<Short> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            return List.of(
                    (short) -32768,
                    (short) -3000,
                    (short) -100,
                    (short) -50,
                    (short) 0,
                    (short) 50,
                    (short) 100,
                    (short) 3000,
                    (short) 32767
            );
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
        public @NotNull List<Short> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            return List.of(
                    (short) 0,
                    (short) 50,
                    (short) 100,
                    (short) 3000,
                    (short) 32767
            );
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
        public @NotNull List<Short> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            return List.of(
                    (short) -32768,
                    (short) -3000,
                    (short) -100,
                    (short) -50,
                    (short) -1
            );
        }
    }

    final class Value extends AllowedNumberInputs.Value<Short> implements AllowedShortInputs {

        Value(short value) {
            super(value);
        }
    }

    final class Values extends AllowedNumberInputs.Values<Short> implements AllowedShortInputs {

        Values(@NotNull List<Short> values) {
            super(values);
        }
    }
}
