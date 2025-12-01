package com.wizardlybump17.wlib.command.input.primitive.number;

import com.wizardlybump17.wlib.command.input.primitive.PrimitiveAllowedInputs;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface AllowedByteInputs extends PrimitiveAllowedInputs<Byte> {

    static @NotNull Range range(byte from, byte to) {
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

    static @NotNull Values values(@NotNull List<Byte> values) {
        return new Values(values);
    }

    static @NotNull Value value(byte value) {
        return new Value(value);
    }

    final class Range extends AllowedNumberInputs.Ranged<Byte> implements AllowedByteInputs {

        Range(byte from, byte to) {
            super(from, to);
            if (from >= to)
                throw new IllegalArgumentException("from must be less than to");
        }

        @Override
        public @NotNull List<Byte> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            byte to = to();
            byte from = from();

            if (to - from < 5) {
                Byte[] bytes = new Byte[to - from];
                for (int i = 0; i < bytes.length; i++)
                    bytes[i] = (byte) (to - from - i);
                return List.of(bytes);
            }

            byte fourth = (byte) ((to - from) / 4);
            return List.of(
                    from,
                    (byte) (from + fourth),
                    (byte) (from + fourth * 2),
                    (byte) (from + fourth * 3),
                    to
            );
        }

        @Override
        public String toString() {
            return "AllowedByteInputs$Range{" +
                    "from=" + from() +
                    ", to=" + to() +
                    "}";
        }
    }

    final class Unlimited extends AllowedNumberInputs.Unlimited<Byte> implements AllowedByteInputs {

        static final @NotNull AllowedByteInputs.Unlimited INSTANCE = new AllowedByteInputs.Unlimited();

        private Unlimited() {
        }

        @Override
        public @NotNull List<Byte> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            return List.of(
                    (byte) -128,
                    (byte) -100,
                    (byte) -50,
                    (byte) 0,
                    (byte) 50,
                    (byte) 100,
                    (byte) 127
            );
        }

        @Override
        public String toString() {
            return "AllowedByteInputs$Unlimited{}";
        }
    }

    final class Positive extends AllowedNumberInputs.Positive<Byte> implements AllowedByteInputs {

        static final @NotNull AllowedByteInputs.Positive INSTANCE = new AllowedByteInputs.Positive();

        private Positive() {
        }

        @Override
        public @NotNull Byte from() {
            return 0;
        }

        @Override
        public @NotNull Byte to() {
            return Byte.MAX_VALUE;
        }

        @Override
        public boolean isInRange(@NotNull Byte input) {
            return input > -1;
        }

        @Override
        public @NotNull List<Byte> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            return List.of(
                    (byte) 0,
                    (byte) 50,
                    (byte) 100,
                    (byte) 127
            );
        }

        @Override
        public String toString() {
            return "AllowedByteInputs$Positive{}";
        }
    }

    final class Negative extends AllowedNumberInputs.Negative<Byte> implements AllowedByteInputs {

        static final @NotNull AllowedByteInputs.Negative INSTANCE = new AllowedByteInputs.Negative();

        private Negative() {
        }

        @Override
        public @NotNull Byte from() {
            return -1;
        }

        @Override
        public @NotNull Byte to() {
            return Byte.MIN_VALUE;
        }

        @Override
        public boolean isInRange(@NotNull Byte input) {
            return input < 0;
        }

        @Override
        public @NotNull List<Byte> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            return List.of(
                    (byte) -128,
                    (byte) -100,
                    (byte) -50,
                    (byte) -1
            );
        }

        @Override
        public String toString() {
            return "AllowedByteInputs$Negative{}";
        }
    }

    final class Value extends AllowedNumberInputs.Value<Byte> implements AllowedByteInputs {

        Value(byte value) {
            super(value);
        }

        @Override
        public String toString() {
            return "AllowedByteInputs$Value{"
                    + "value=" + value()
                    + '}';
        }
    }

    final class Values extends AllowedNumberInputs.Values<Byte> implements AllowedByteInputs {

        Values(@NotNull List<Byte> values) {
            super(values);
        }

        @Override
        public String toString() {
            return "AllowedByteInputs$Values{"
                    + "values=" + allowedValues()
                    + '}';
        }
    }
}
