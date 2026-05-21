package com.wizardlybump17.wlib.command.exception;

import org.jetbrains.annotations.NotNull;

public class InvalidInputException extends Exception {

    public InvalidInputException(@NotNull String message) {
        super(message);
    }

    public InvalidInputException(@NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
    }
}
