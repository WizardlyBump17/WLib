package com.wizardlybump17.wlib.command.suggestion.primitive.number;

import com.wizardlybump17.wlib.command.suggestion.AbstractValuesSuggester;
import com.wizardlybump17.wlib.command.suggestion.primitive.PrimitiveSuggester;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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

    final class Values extends AbstractValuesSuggester<Integer> implements IntegerSuggester {

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
