package com.wizardlybump17.wlib.command.result.empty;

import com.wizardlybump17.wlib.command.node.CommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class EmptySuccess<T> implements EmptyResult<T> {

    static final @NotNull EmptySuccess<?> INSTANCE = new EmptySuccess<>();

    @Override
    public boolean success() {
        return true;
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
        return "EmptySuccess{}";
    }
}
