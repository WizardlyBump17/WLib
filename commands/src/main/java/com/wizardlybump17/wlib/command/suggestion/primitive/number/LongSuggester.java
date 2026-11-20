package com.wizardlybump17.wlib.command.suggestion.primitive.number;

import com.wizardlybump17.wlib.command.suggestion.AbstractValuesSuggester;
import com.wizardlybump17.wlib.command.suggestion.primitive.PrimitiveSuggester;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface LongSuggester extends PrimitiveSuggester<Long>, NumberSuggester<Long> {

    static @NotNull Values value(long value) {
        return new Values(List.of(value));
    }

    static @NotNull Values values(@NotNull List<Long> values) {
        return new Values(values);
    }

    static @NotNull Values values(long @NotNull ... values) {
        List<Long> list = new ArrayList<>(values.length);
        for (long value : values)
            list.add(value);
        return new Values(List.copyOf(list));
    }

    static @NotNull Values range(long from, long to, int amount) {
        if (to - from < amount) {
            Long[] longs = new Long[(int) (to - from)];
            for (int i = 0; i < longs.length; i++)
                longs[i] = to - from - i;
            return new Values(List.of(longs));
        }

        long part = (to - from) / amount;
        List<Long> values = new ArrayList<>(amount);
        for (long i = 0; i < amount; i++)
            values.add(from + part * amount);
        return new Values(List.copyOf(values));
    }

    static @NotNull Values range(long from, long to) {
        return range(from, to, 4);
    }

    static @NotNull Values positive() {
        return Values.POSITIVE;
    }

    static @NotNull Values negative() {
        return Values.NEGATIVE;
    }

    static @NotNull Values unlimited() {
        return Values.UNLIMITED;
    }

    final class Values extends AbstractValuesSuggester<Long> implements LongSuggester {

        private static final @NotNull LongSuggester.Values POSITIVE = new LongSuggester.Values(List.of(
                0L,
                50L,
                200L,
                3000L,
                50000L,
                100000L
        ));
        private static final @NotNull LongSuggester.Values NEGATIVE = new LongSuggester.Values(List.of(
                -100000L,
                -50000L,
                -3000L,
                -200L,
                -50L,
                -1L
        ));
        private static final @NotNull LongSuggester.Values UNLIMITED = new LongSuggester.Values(List.of(
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
        ));

        Values(@NotNull List<Long> values) {
            super(values);
        }

        @Override
        public String toString() {
            return "LongSuggester$Values{" +
                    "values=" + values() +
                    '}';
        }
    }
}
