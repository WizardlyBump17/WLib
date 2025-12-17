package com.wizardlybump17.wlib.command.result;

import com.wizardlybump17.wlib.command.node.CommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record SuccessResult<T>(int lastInputIndex, @NotNull CommandNode<?> lastNode, @Nullable T data) implements CommandResult<T> {

    public static final @NotNull String ID = "WLib:Success";

    @Override
    public boolean success() {
        return true;
    }

    @Override
    public @NotNull String id() {
        return ID;
    }
}
