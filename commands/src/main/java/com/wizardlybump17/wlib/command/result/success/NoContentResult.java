package com.wizardlybump17.wlib.command.result.success;

import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.result.CommandResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record NoContentResult<T>(int lastInputIndex, @NotNull CommandNode<?> lastNode) implements CommandResult<T> {

    private static final @NotNull String ID = "WLib:Success/NoContent";

    @Override
    public @NotNull String id() {
        return ID;
    }

    @Override
    public boolean success() {
        return true;
    }

    @Override
    public @Nullable T data() {
        return null;
    }
}
