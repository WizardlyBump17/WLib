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

    final class Values extends AbstractValuesSuggester<Short> implements ShortSuggester {

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
