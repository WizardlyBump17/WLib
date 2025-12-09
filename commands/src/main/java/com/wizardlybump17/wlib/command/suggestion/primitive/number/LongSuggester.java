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

    final class Values extends AbstractValuesSuggester<Long> implements LongSuggester {

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
