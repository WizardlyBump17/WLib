package com.wizardlybump17.wlib.command.suggestion.primitive;

import com.wizardlybump17.wlib.command.suggestion.AbstractValuesSuggester;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface CharacterSuggester extends PrimitiveSuggester<Character> {

    static @NotNull Values value(char value) {
        return new Values(List.of(value));
    }

    static @NotNull Values values(@NotNull List<Character> values) {
        return new Values(values);
    }

    static @NotNull Values values(char @NotNull ... values) {
        List<Character> list = new ArrayList<>(values.length);
        for (char value : values)
            list.add(value);
        return new Values(List.copyOf(list));
    }

    static @NotNull Values range(char from, char to, int amount) {
        if (to - from < amount) {
            Character[] values = new Character[(int) (to - from)];
            for (int i = 0; i < values.length; i++)
                values[i] = (char) (to - from - i);
            return new Values(List.of(values));
        }

        char part = (char) ((to - from) / amount);
        List<Character> values = new ArrayList<>(amount);
        for (char i = 0; i < amount; i++)
            values.add((char) (from + part * amount));
        return new Values(List.copyOf(values));
    }

    static @NotNull Values range(char from, char to) {
        return range(from, to, 4);
    }

    static @NotNull Values any() {
        return Values.ANY;
    }

    final class Values extends AbstractValuesSuggester<Character> implements CharacterSuggester {

        private static final @NotNull Values ANY = new Values(List.of());

        Values(@NotNull List<Character> values) {
            super(values);
        }

        @Override
        public String toString() {
            return "CharacterSuggester$Values{" +
                    "values=" + values() +
                    '}';
        }
    }
}
