package com.wizardlybump17.wlib.command.result.error;

import com.wizardlybump17.wlib.command.node.CommandNode;
import org.jetbrains.annotations.NotNull;

public record ConflictResult<T>(int lastInputIndex, @NotNull CommandNode<?> lastNode, @NotNull ErrorDetails errorDetails) implements UnsuccessResult<T> {

    public static final @NotNull String ID = "WLib:Error/Conflict";

    @Override
    public @NotNull String id() {
        return ID;
    }
}
