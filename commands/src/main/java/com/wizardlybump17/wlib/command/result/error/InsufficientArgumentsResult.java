package com.wizardlybump17.wlib.command.result.error;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.node.CommandNode;
import org.jetbrains.annotations.NotNull;

public record InsufficientArgumentsResult<T>(@NotNull Command command) implements UnsuccessResult<T> {

    @Override
    public int lastInputIndex() {
        return -1;
    }

    @Override
    public @NotNull CommandNode<?> lastNode() {
        return command.getRoot();
    }
}
