package com.wizardlybump17.wlib.command.exception;

import org.jetbrains.annotations.NotNull;

public class SuggesterException extends Exception {

    public SuggesterException() {
    }

    public SuggesterException(@NotNull String message) {
        super(message);
    }

    public SuggesterException(@NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
    }

    public SuggesterException(@NotNull Throwable cause) {
        super(cause);
    }
}
