package com.wizardlybump17.wlib.command.rework.result;

import com.wizardlybump17.wlib.command.rework.node.CommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record SuccessResult<T>(int lastInputIndex, @NotNull CommandNode<?> lastNode, @Nullable T data) implements CommandResult<T> {

    @Override
    public boolean success() {
        return true;
    }
}
