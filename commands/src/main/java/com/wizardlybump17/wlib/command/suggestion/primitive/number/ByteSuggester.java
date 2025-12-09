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

    final class Values extends AbstractValuesSuggester<Byte> implements ByteSuggester {

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
