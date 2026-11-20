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

    static @NotNull Values range(float from, float to, int amount) {
        float part = (to - from) / amount;
        List<Float> values = new ArrayList<>(amount);
        for (float i = 0; i < amount; i++)
            values.add(from + part * amount);
        return new Values(List.copyOf(values));
    }

    static @NotNull Values range(float from, float to) {
        return range(from, to, 4);
    }

    static @NotNull Values positive() {
        return new Values(List.of(
                0.0F,
                50.5F,
                200.5F,
                3000.5F,
                50000.5F,
                100000.5F
        ));
    }

    static @NotNull Values negative() {
        return new Values(List.of(
                -100000.5F,
                -50000.5F,
                -3000.5F,
                -200.5F,
                -50.5F,
                -1.0F
        ));
    }

    static @NotNull Values unlimited() {
        return new Values(List.of(
                -100000.5F,
                -50000.5F,
                -3000.5F,
                -200.5F,
                -50.5F,
                0.0F,
                50.5F,
                200.5F,
                3000.5F,
                50000.5F,
                100000.5F
        ));
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
