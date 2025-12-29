package com.wizardlybump17.wlib.command.suggestion.primitive.number;

import com.wizardlybump17.wlib.command.suggestion.AbstractValuesSuggester;
import com.wizardlybump17.wlib.command.suggestion.primitive.PrimitiveSuggester;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public interface IntegerSuggester extends PrimitiveSuggester<Integer>, NumberSuggester<Integer> {

    static @NotNull Values value(int value) {
        return new Values(List.of(value));
    }

    static @NotNull Values values(@NotNull List<Integer> values) {
        return new Values(values);
    }

    static @NotNull Values values(int @NotNull ... values) {
        List<Integer> list = new ArrayList<>(values.length);
        for (int value : values)
            list.add(value);
        return new Values(List.copyOf(list));
    }

    static @NotNull Values range(int from, int to, int amount) {
        if (to - from < amount)
            return new Values(IntStream.rangeClosed(from, to).boxed().toList());

        int part = (to - from) / amount;
        List<Integer> values = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++)
            values.add(from + part * i);
        values.add(to);
        return new Values(List.copyOf(values));
    }

    static @NotNull Values range(int from, int to) {
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

    final class Values extends AbstractValuesSuggester<Integer> implements IntegerSuggester {

        private static final @NotNull IntegerSuggester.Values POSITIVE = new IntegerSuggester.Values(List.of(
                0,
                50,
                200,
                3000,
                50000,
                100000
        ));
        private static final @NotNull IntegerSuggester.Values NEGATIVE = new IntegerSuggester.Values(List.of(
                -100000,
                -50000,
                -3000,
                -200,
                -50,
                -1
        ));
        private static final @NotNull IntegerSuggester.Values UNLIMITED = new IntegerSuggester.Values(List.of(
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
        ));

        Values(@NotNull List<Integer> values) {
            super(values);
        }

        @Override
        public String toString() {
            return "IntegerSuggester$Values{" +
                    "values=" + values() +
                    '}';
        }
    }
}
