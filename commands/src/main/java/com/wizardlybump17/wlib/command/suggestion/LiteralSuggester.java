package com.wizardlybump17.wlib.command.suggestion;

import com.wizardlybump17.wlib.command.exception.SuggesterException;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public final class LiteralSuggester implements Suggester<String> {

    private final @NotNull List<String> suggestions;

    private LiteralSuggester(@NotNull String value) {
        this.suggestions = List.of(value);
    }

    @Override
    public @NotNull List<String> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current, @NotNull CommandNode<?> currentNode) throws SuggesterException {
        return suggestions;
    }

    public static @NotNull LiteralSuggester of(@NotNull String value) {
        return new LiteralSuggester(value);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        LiteralSuggester that = (LiteralSuggester) o;
        return Objects.equals(suggestions, that.suggestions);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(suggestions);
    }

    @Override
    public String toString() {
        return "LiteralSuggester{" +
                "value=" + suggestions.getFirst() +
                '}';
    }
}
