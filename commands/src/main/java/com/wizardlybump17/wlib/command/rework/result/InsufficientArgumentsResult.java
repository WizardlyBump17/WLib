package com.wizardlybump17.wlib.command.rework.result;

import com.wizardlybump17.wlib.command.rework.node.CommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record InsufficientArgumentsResult<T>(int index, @NotNull CommandNode<?> node) implements CommandResult<T> {

    @Override
    public boolean success() {
        return false;
    }

    @Override
    public @Nullable T data() {
        return null;
    }
}
