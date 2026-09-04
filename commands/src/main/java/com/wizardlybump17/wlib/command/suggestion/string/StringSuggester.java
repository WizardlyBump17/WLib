package com.wizardlybump17.wlib.command.suggestion.string;

import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.sender.CommandSender;
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

    static @NotNull Any any() {
        return Any.INSTANCE;
    }

    @Override
    default boolean needsEscape() {
        return true;
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

    final class Any implements StringSuggester {

        private static final @NotNull Any INSTANCE = new Any();

        @Override
        public @NotNull List<String> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current, @NotNull CommandNode<?> currentNode) {
            return List.of(current);
        }
    }
}
