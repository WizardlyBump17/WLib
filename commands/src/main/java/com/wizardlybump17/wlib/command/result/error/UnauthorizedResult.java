package com.wizardlybump17.wlib.command.result.error;

import com.wizardlybump17.wlib.command.node.CommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record UnauthorizedResult<T>(int lastInputIndex, @NotNull CommandNode<?> lastNode, @Nullable String message) implements UnsuccessResult<T> {

    public static final @NotNull String ID = "WLib:Error/Unauthorized";

    @Override
    public @NotNull String id() {
        return ID;
    }
}
