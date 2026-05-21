package com.wizardlybump17.wlib.command.exception;

import com.wizardlybump17.wlib.command.node.CommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandExecutionException extends RuntimeException {

    private final int lastInputIndex;
    private final @Nullable CommandNode<?> lastNode;
    private final @NotNull Reason reason;

    public CommandExecutionException(int lastInputIndex, @Nullable CommandNode<?> lastNode, @NotNull Reason reason) {
        this.lastInputIndex = lastInputIndex;
        this.lastNode = lastNode;
        this.reason = reason;
    }

    public CommandExecutionException(@NotNull String message, int lastInputIndex, @Nullable CommandNode<?> lastNode, @NotNull Reason reason) {
        super(message);
        this.lastInputIndex = lastInputIndex;
        this.lastNode = lastNode;
        this.reason = reason;
    }

    public CommandExecutionException(@NotNull String message, @NotNull Throwable cause, int lastInputIndex, @Nullable CommandNode<?> lastNode, @NotNull Reason reason) {
        super(message, cause);
        this.lastInputIndex = lastInputIndex;
        this.lastNode = lastNode;
        this.reason = reason;
    }

    public CommandExecutionException(@NotNull Throwable cause, int lastInputIndex, @Nullable CommandNode<?> lastNode, @NotNull Reason reason) {
        super(cause);
        this.lastInputIndex = lastInputIndex;
        this.lastNode = lastNode;
        this.reason = reason;
    }

    public enum Reason {

        EMPTY_INPUT,
        PARSING_ERROR,
        INVALID_INPUT,
        EXTRA_INPUT,
        NO_COMMAND_EXECUTOR,
        COMMAND_NOT_FOUND,
        NULL_INPUT_NOT_ACCEPTED,
        INVALID_COMMAND_RESULT,
        GENERIC
    }
}
