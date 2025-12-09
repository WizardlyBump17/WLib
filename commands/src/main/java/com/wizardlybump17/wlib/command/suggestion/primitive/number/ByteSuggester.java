package com.wizardlybump17.wlib.command.suggestion.primitive.number;

import com.wizardlybump17.wlib.command.suggestion.AbstractValuesSuggester;
import com.wizardlybump17.wlib.command.suggestion.primitive.PrimitiveSuggester;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface ByteSuggester extends PrimitiveSuggester<Byte>, NumberSuggester<Byte> {

    static @NotNull Values value(byte value) {
        return new Values(List.of(value));
    }

    static @NotNull Values values(@NotNull List<Byte> values) {
        return new Values(values);
    }

    static @NotNull Values values(byte @NotNull ... values) {
        List<Byte> list = new ArrayList<>(values.length);
        for (byte value : values)
            list.add(value);
        return new Values(List.copyOf(list));
    }

    static @NotNull Values range(byte from, byte to, int amount) {
        if (to - from < amount) {
            Byte[] values = new Byte[to - from];
            for (int i = 0; i < values.length; i++)
                values[i] = (byte) (to - from - i);
            return new Values(List.of(values));
        }

        byte part = (byte) ((to - from) / amount);
        List<Byte> values = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++)
            values.add((byte) (from + part * amount));
        return new Values(List.copyOf(values));
    }

    static @NotNull Values range(byte from, byte to) {
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

    final class Values extends AbstractValuesSuggester<Byte> implements ByteSuggester {

        private static final @NotNull Values POSITIVE = new Values(List.of(
                (byte) 0,
                (byte) 50,
                (byte) 100,
                (byte) 127
        ));
        private static final @NotNull Values NEGATIVE = new Values(List.of(
                (byte) -128,
                (byte) -100,
                (byte) -50,
                (byte) -1
        ));
        private static final @NotNull Values UNLIMITED = new Values(List.of(
                (byte) -128,
                (byte) -100,
                (byte) -50,
                (byte) 0,
                (byte) 50,
                (byte) 100,
                (byte) 127
        ));

        Values(@NotNull List<Byte> values) {
            super(values);
        }

        @Override
        public String toString() {
            return "ByteSuggester$Values{" +
                    "values=" + values() +
                    '}';
        }
    }
}
