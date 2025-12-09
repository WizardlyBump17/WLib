package com.wizardlybump17.wlib.command.suggestion.primitive.number;

import com.wizardlybump17.wlib.command.suggestion.AbstractValuesSuggester;
import com.wizardlybump17.wlib.command.suggestion.primitive.PrimitiveSuggester;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface FloatSuggester extends PrimitiveSuggester<Float>, NumberSuggester<Float> {

    static @NotNull Values value(float value) {
        return new Values(List.of(value));
    }

    static @NotNull Values values(@NotNull List<Float> values) {
        return new Values(values);
    }

    static @NotNull Values values(float @NotNull ... values) {
        List<Float> list = new ArrayList<>(values.length);
        for (float value : values)
            list.add(value);
        return new Values(List.copyOf(list));
    }

    final class Values extends AbstractValuesSuggester<Float> implements FloatSuggester {

        Values(@NotNull List<Float> values) {
            super(values);
        }

        @Override
        public String toString() {
            return "FloatSuggester$Values{" +
                    "values=" + values() +
                    '}';
        }
    }
}
