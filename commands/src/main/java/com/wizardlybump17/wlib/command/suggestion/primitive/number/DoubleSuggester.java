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

    static @NotNull Values range(double from, double to, int amount) {
        double part = (to - from) / amount;
        List<Double> values = new ArrayList<>(amount);
        for (double i = 0; i < amount; i++)
            values.add(from + part * amount);
        return new Values(List.copyOf(values));
    }

    static @NotNull Values range(double from, double to) {
        return range(from, to, 4);
    }

    static @NotNull Values positive() {
        return new Values(List.of(
                0.0,
                50.5,
                200.5,
                3000.5,
                50000.5,
                100000.5
        ));
    }

    static @NotNull Values negative() {
        return new Values(List.of(
                -100000.5,
                -50000.5,
                -3000.5,
                -200.5,
                -50.5,
                -1.0
        ));
    }

    static @NotNull Values unlimited() {
        return new Values(List.of(
                -100000.5,
                -50000.5,
                -3000.5,
                -200.5,
                -50.5,
                0.0,
                50.5,
                200.5,
                3000.5,
                50000.5,
                100000.5
        ));
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
