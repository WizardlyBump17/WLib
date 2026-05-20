package com.wizardlybump17.wlib.command.result.error;

import com.wizardlybump17.wlib.command.node.CommandNode;
import org.jetbrains.annotations.NotNull;

public record GenericErrorResult<T>(@NotNull ErrorDetails errorDetails) implements UnsuccessResult<T> {

    public static final @NotNull String DEFAULT_MESSAGE = "An error occurred while executing the command.";
    public static final @NotNull String ID = "WLib:Unsuccess/Generic";

    public GenericErrorResult(int lastInputIndex, @NotNull CommandNode<?> lastNode) {
        this(lastInputIndex, lastNode, DEFAULT_MESSAGE);
    }

    @Override
    public @NotNull String id() {
        return ID;
    }
}
