package com.wizardlybump17.wlib.command.suggestion.string;

import com.wizardlybump17.wlib.command.suggestion.AbstractValuesSuggester;
import com.wizardlybump17.wlib.command.suggestion.primitive.PrimitiveSuggester;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface StringSuggester extends PrimitiveSuggester<String> {

    static @NotNull Values value(String value) {
        return new Values(List.of(value));
    }

    static @NotNull Values values(@NotNull List<String> values) {
        return new Values(values);
    }

    static @NotNull Values values(String @NotNull ... values) {
        return new Values(List.of(values));
    }

    final class Values extends AbstractValuesSuggester<String> implements StringSuggester {

        Values(@NotNull List<String> values) {
            super(values);
        }

        @Override
        public String toString() {
            return "StringSuggester$Values{" +
                    "values=" + values() +
                    '}';
        }
    }
}
