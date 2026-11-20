package com.wizardlybump17.wlib.command.suggestion.primitive.number;

import com.wizardlybump17.wlib.command.suggestion.AbstractValuesSuggester;
import com.wizardlybump17.wlib.command.suggestion.primitive.PrimitiveSuggester;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface ShortSuggester extends PrimitiveSuggester<Short>, NumberSuggester<Short> {

    static @NotNull Values value(short value) {
        return new Values(List.of(value));
    }

    static @NotNull Values values(@NotNull List<Short> values) {
        return new Values(values);
    }

    static @NotNull Values values(short @NotNull ... values) {
        List<Short> list = new ArrayList<>(values.length);
        for (short value : values)
            list.add(value);
        return new Values(List.copyOf(list));
    }

    static @NotNull Values range(short from, short to, int amount) {
        if (to - from < amount) {
            Short[] values = new Short[to - from];
            for (int i = 0; i < values.length; i++)
                values[i] = (short) (to - from - i);
            return new Values(List.of(values));
        }

        short part = (short) ((to - from) / amount);
        List<Short> values = new ArrayList<>(amount);
        for (short i = 0; i < amount; i++)
            values.add((short) (from + part * amount));
        return new Values(List.copyOf(values));
    }

    static @NotNull Values range(short from, short to) {
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

    final class Values extends AbstractValuesSuggester<Short> implements ShortSuggester {

        private static final @NotNull ShortSuggester.Values POSITIVE = new ShortSuggester.Values(List.of(
                (short) 0,
                (short) 50,
                (short) 100,
                (short) 3000,
                (short) 32767
        ));
        private static final @NotNull ShortSuggester.Values NEGATIVE = new ShortSuggester.Values(List.of(
                (short) -32768,
                (short) -3000,
                (short) -100,
                (short) -50,
                (short) -1
        ));
        private static final @NotNull ShortSuggester.Values UNLIMITED = new ShortSuggester.Values(List.of(
                (short) -32768,
                (short) -3000,
                (short) -100,
                (short) -50,
                (short) 0,
                (short) 50,
                (short) 100,
                (short) 3000,
                (short) 32767
        ));

        Values(@NotNull List<Short> values) {
            super(values);
        }

        @Override
        public String toString() {
            return "ShortSuggester$Values{" +
                    "values=" + values() +
                    '}';
        }
    }
}
