package com.wizardlybump17.wlib.command.suggestion;

import com.wizardlybump17.wlib.command.exception.SuggesterException;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Suggester<T> {

    @NotNull List<T> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current, @NotNull CommandNode<?> currentNode) throws SuggesterException;

    default @NotNull String getStringRepresentation(@NotNull T value) {
        return value.toString();
    }

    default boolean needsEscape() {
        return false;
    }
}
