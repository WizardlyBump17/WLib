package com.wizardlybump17.wlib.command.rework.result.error;

import com.wizardlybump17.wlib.command.rework.node.CommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractUnsuccessResult<T> implements UnsuccessResult<T> {

    private final int lastInputIndex;
    private final @NotNull CommandNode<?> lastNode;

    public AbstractUnsuccessResult(int lastInputIndex, @NotNull CommandNode<?> lastNode) {
        this.lastInputIndex = lastInputIndex;
        this.lastNode = lastNode;
    }

    @Override
    public final boolean success() {
        return false;
    }

    @Override
    public final @Nullable T data() {
        return null;
    }

    @Override
    public int lastInputIndex() {
        return lastInputIndex;
    }

    @Override
    public @NotNull CommandNode<?> lastNode() {
        return lastNode;
    }
}
