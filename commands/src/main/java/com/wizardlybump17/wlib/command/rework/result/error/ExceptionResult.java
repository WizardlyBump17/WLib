package com.wizardlybump17.wlib.command.rework.result.error;

import com.wizardlybump17.wlib.command.rework.node.CommandNode;
import org.jetbrains.annotations.NotNull;

public final class ExceptionResult<T> extends AbstractUnsuccessResult<T> {

    private final @NotNull Throwable exception;

    public ExceptionResult(int lastInputIndex, @NotNull CommandNode<?> lastNode, @NotNull Throwable exception) {
        super(lastInputIndex, lastNode);
        this.exception = exception;
    }

    public @NotNull Throwable exception() {
        return exception;
    }
}
