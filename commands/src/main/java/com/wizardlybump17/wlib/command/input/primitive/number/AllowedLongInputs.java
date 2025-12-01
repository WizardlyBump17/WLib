package com.wizardlybump17.wlib.command.input.primitive.number;

import com.wizardlybump17.wlib.command.input.primitive.PrimitiveAllowedInputs;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface AllowedLongInputs extends PrimitiveAllowedInputs<Long> {

    static @NotNull Range range(long from, long to) {
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

    static @NotNull Values values(@NotNull List<Long> values) {
        return new Values(values);
    }

    static @NotNull Value value(long value) {
        return new Value(value);
    }

    final class Range extends AllowedNumberInputs.Ranged<Long> implements AllowedLongInputs {

        Range(long from, long to) {
            super(from, to);
            if (from >= to)
                throw new IllegalArgumentException("from must be less than to");
        }

        @Override
        public @NotNull List<Long> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            long to = to();
            long from = from();

            if (to - from < 5) {
                Long[] longs = new Long[(int) (to - from)];
                for (int i = 0; i < longs.length; i++)
                    longs[i] = to - from - i;
                return List.of(longs);
            }

            long fourth = (to - from) / 4;
            return List.of(
                    from,
                    from + fourth,
                    from + fourth * 2,
                    from + fourth * 3,
                    to
            );
        }

        @Override
        public String toString() {
            return "AllowedLongInputs$Range{" +
                    "from=" + from() +
                    ", to=" + to() +
                    "}";
        }
    }

    final class Unlimited extends AllowedNumberInputs.Unlimited<Long> implements AllowedLongInputs {

        static final @NotNull AllowedLongInputs.Unlimited INSTANCE = new AllowedLongInputs.Unlimited();

        private Unlimited() {
        }

        @Override
        public @NotNull List<Long> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            return List.of(
                    -100000L,
                    -50000L,
                    -3000L,
                    -200L,
                    -50L,
                    0L,
                    50L,
                    200L,
                    3000L,
                    50000L,
                    100000L
            );
        }

        @Override
        public String toString() {
            return "AllowedLongInputs$Unlimited{}";
        }
    }

    final class Positive extends AllowedNumberInputs.Positive<Long> implements AllowedLongInputs {

        static final @NotNull AllowedLongInputs.Positive INSTANCE = new AllowedLongInputs.Positive();

        private Positive() {
        }

        @Override
        public @NotNull Long from() {
            return 0L;
        }

        @Override
        public @NotNull Long to() {
            return Long.MAX_VALUE;
        }

        @Override
        public boolean isInRange(@NotNull Long input) {
            return input > -1;
        }

        @Override
        public @NotNull List<Long> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            return List.of(
                    0L,
                    50L,
                    200L,
                    3000L,
                    50000L,
                    100000L
            );
        }

        @Override
        public String toString() {
            return "AllowedLongInputs$Positive{}";
        }
    }

    final class Negative extends AllowedNumberInputs.Negative<Long> implements AllowedLongInputs {

        static final @NotNull AllowedLongInputs.Negative INSTANCE = new AllowedLongInputs.Negative();

        private Negative() {
        }

        @Override
        public @NotNull Long from() {
            return -1L;
        }

        @Override
        public @NotNull Long to() {
            return Long.MIN_VALUE;
        }

        @Override
        public boolean isInRange(@NotNull Long input) {
            return input < 0;
        }

        @Override
        public @NotNull List<Long> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            return List.of(
                    -100000L,
                    -50000L,
                    -3000L,
                    -200L,
                    -50L,
                    -1L
            );
        }

        @Override
        public String toString() {
            return "AllowedLongInputs$Negative{}";
        }
    }

    final class Value extends AllowedNumberInputs.Value<Long> implements AllowedLongInputs {

        Value(long value) {
            super(value);
        }

        @Override
        public String toString() {
            return "AllowedLongInputs$Value{" +
                    "value=" + value() +
                    "}";
        }
    }

    final class Values extends AllowedNumberInputs.Values<Long> implements AllowedLongInputs {

        Values(@NotNull List<Long> values) {
            super(values);
        }

        @Override
        public String toString() {
            return "AllowedLongInputs$Values{"
                    + "values=" + allowedValues()
                    + '}';
        }
    }
}
