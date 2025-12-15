package com.wizardlybump17.wlib.command.suggestion;

import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public abstract class AbstractValuesSuggester<T> implements ValuesSuggester<T> {

    private final @NotNull List<T> suggestions;

    public AbstractValuesSuggester(@NotNull List<T> suggestions) {
        this.suggestions = List.copyOf(suggestions);
    }

    @Override
    public @NotNull List<T> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current, @NotNull CommandNode<?> currentNode) {
        return suggestions;
    }

    @Override
    public @NotNull List<T> values() {
        return suggestions;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass())
            return false;
        AbstractValuesSuggester<?> that = (AbstractValuesSuggester<?>) object;
        return Objects.equals(suggestions, that.suggestions);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(suggestions);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "suggestions=" + suggestions +
                '}';
    }
}
