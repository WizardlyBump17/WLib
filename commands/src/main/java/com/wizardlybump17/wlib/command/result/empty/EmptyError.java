package com.wizardlybump17.wlib.command.result.empty;

import com.wizardlybump17.wlib.command.node.CommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class EmptyError<T> implements EmptyResult<T> {

    static final @NotNull EmptyError<?> INSTANCE = new EmptyError<>();

    @Override
    public boolean success() {
        return false;
    }

    @Override
    public @Nullable T data() {
        return null;
    }

    @Override
    public @NotNull CommandNode<?> lastNode() {
        return EmptyCommandNode.INSTANCE;
    }

    @Override
    public String toString() {
        return "EmptyError{}";
    }
}
