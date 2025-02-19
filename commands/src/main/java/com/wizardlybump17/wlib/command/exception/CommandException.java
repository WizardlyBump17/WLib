package com.wizardlybump17.wlib.command.exception;

import org.jetbrains.annotations.NotNull;

public class CommandException extends Exception {

    public CommandException() {
    }

    public CommandException(@NotNull String message) {
        super(message);
    }

    public CommandException(@NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
    }

    public CommandException(@NotNull Throwable cause) {
        super(cause);
    }
}
