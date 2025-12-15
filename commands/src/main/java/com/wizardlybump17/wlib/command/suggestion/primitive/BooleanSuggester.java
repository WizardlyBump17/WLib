package com.wizardlybump17.wlib.command.suggestion.primitive;

import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface BooleanSuggester extends PrimitiveSuggester<Boolean> {

    static @NotNull True trueValue() {
        return True.INSTANCE;
    }

    static @NotNull False falseValue() {
        return False.INSTANCE;
    }

    static @NotNull Any any() {
        return Any.INSTANCE;
    }

    final class True implements BooleanSuggester {

        private static final @NotNull List<Boolean> LIST = List.of(true);
        private static final @NotNull True INSTANCE = new True();

        private True() {
        }

        @Override
        public @NotNull List<Boolean> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current, @NotNull CommandNode<?> currentNode) {
            return LIST;
        }

        @Override
        public String toString() {
            return "BooleanSuggester$True{}";
        }
    }

    final class False implements BooleanSuggester {

        private static final @NotNull List<Boolean> LIST = List.of(false);
        private static final @NotNull False INSTANCE = new False();

        private False() {
        }

        @Override
        public @NotNull List<Boolean> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current, @NotNull CommandNode<?> currentNode) {
            return LIST;
        }

        @Override
        public String toString() {
            return "BooleanSuggester$False{}";
        }
    }

    final class Any implements BooleanSuggester {

        private static final @NotNull List<Boolean> LIST = List.of(true, false);
        private static final @NotNull Any INSTANCE = new Any();

        private Any() {
        }

        @Override
        public @NotNull List<Boolean> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current, @NotNull CommandNode<?> currentNode) {
            return LIST;
        }

        @Override
        public String toString() {
            return "BooleanSuggester$Any{}";
        }
    }
}
