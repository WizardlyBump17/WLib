package com.wizardlybump17.wlib.command.rework.result;

import com.wizardlybump17.wlib.command.rework.node.CommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class InvalidArgumentResult<T> implements CommandResult<T> {

    private final @NotNull CommandNode<T> node;
    private final @Nullable T data;
    private final @Nullable CommandResult<T> previousResult;

    private InvalidArgumentResult(@NotNull CommandNode<T> node, @Nullable T data, @Nullable CommandResult<T> previousResult) {
        this.node = node;
        this.data = data;
        this.previousResult = previousResult;
    }

    public InvalidArgumentResult(@NotNull CommandNode<T> node, @NotNull CommandResult<T> previousResult) {
        this(node, previousResult.data(), previousResult);
    }

    public InvalidArgumentResult(@NotNull CommandNode<T> node, @Nullable T data) {
        this(node, data, null);
    }

    public InvalidArgumentResult(@NotNull CommandNode<T> node) {
        this(node, null, null);
    }

    @Override
    public boolean success() {
        return false;
    }

    @Override
    public @Nullable T data() {
        return data;
    }

    public @NotNull CommandNode<T> node() {
        return node;
    }

    public @Nullable CommandResult<T> previousResult() {
        return previousResult;
    }
}
