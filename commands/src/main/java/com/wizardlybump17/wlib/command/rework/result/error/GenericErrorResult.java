package com.wizardlybump17.wlib.command.rework.result.error;

import com.wizardlybump17.wlib.command.rework.node.CommandNode;
import org.jetbrains.annotations.NotNull;

public record GenericErrorResult<T>(int lastInputIndex, @NotNull CommandNode<?> lastNode, @NotNull String message) implements UnsuccessResult<T> {

    public static final @NotNull String DEFAULT_MESSAGE = "An error occurred while executing the command.";

    public GenericErrorResult(int lastInputIndex, @NotNull CommandNode<?> lastNode) {
        this(lastInputIndex, lastNode, DEFAULT_MESSAGE);
    }
}
