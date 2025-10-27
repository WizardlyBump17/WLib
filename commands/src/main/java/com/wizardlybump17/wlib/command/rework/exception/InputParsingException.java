package com.wizardlybump17.wlib.command.rework.exception;

import org.jetbrains.annotations.NotNull;

public class InputParsingException extends Exception {

    public InputParsingException(@NotNull String message) {
        super(message);
    }

    public InputParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
