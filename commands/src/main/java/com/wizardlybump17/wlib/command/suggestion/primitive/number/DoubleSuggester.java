package com.wizardlybump17.wlib.command.suggestion.primitive.number;

import com.wizardlybump17.wlib.command.suggestion.AbstractValuesSuggester;
import com.wizardlybump17.wlib.command.suggestion.primitive.PrimitiveSuggester;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface DoubleSuggester extends PrimitiveSuggester<Double>, NumberSuggester<Double> {

    static @NotNull Values value(double value) {
        return new Values(List.of(value));
    }

    static @NotNull Values values(@NotNull List<Double> values) {
        return new Values(values);
    }

    static @NotNull Values values(double @NotNull ... values) {
        List<Double> list = new ArrayList<>(values.length);
        for (double value : values)
            list.add(value);
        return new Values(List.copyOf(list));
    }

    final class Values extends AbstractValuesSuggester<Double> implements DoubleSuggester {

        Values(@NotNull List<Double> values) {
            super(values);
        }

        @Override
        public String toString() {
            return "DoubleSuggester$Values{" +
                    "values=" + values() +
                    '}';
        }
    }
}
