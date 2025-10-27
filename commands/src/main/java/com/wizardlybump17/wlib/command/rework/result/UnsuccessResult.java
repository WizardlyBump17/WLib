package com.wizardlybump17.wlib.command.rework.result;

import com.wizardlybump17.wlib.command.rework.node.CommandNode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface UnsuccessResult<T> extends CommandResult<T> {

    @Override
    default boolean success() {
        return false;
    }

    @Contract("-> null")
    @Override
    default @Nullable T data() {
        return null;
    }

    int lastInputIndex();

    @NotNull CommandNode<?> lastNode();
}
