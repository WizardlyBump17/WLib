package com.wizardlybump17.wlib.command.result.error;

import com.wizardlybump17.wlib.command.node.CommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ForbiddenResult<T>(int lastInputIndex, @NotNull CommandNode<?> lastNode, @Nullable String message, @NotNull ErrorDetails errorDetails) implements UnsuccessResult<T> {

    public static final @NotNull String ID = "WLib:Error/Forbidden";

    @Override
    public @NotNull String id() {
        return ID;
    }
}
