package com.wizardlybump17.wlib.command.exception;

import org.jetbrains.annotations.NotNull;

public class CommandExecutionException extends RuntimeException {

    public static final @NotNull String MESSAGE = "Error while executing the command %s, index %s, node %s";

    public CommandExecutionException() {
    }

    public CommandExecutionException(@NotNull String message) {
        super(message);
    }

    public CommandExecutionException(@NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
    }

    public CommandExecutionException(@NotNull Throwable cause) {
        super(cause);
    }
}
