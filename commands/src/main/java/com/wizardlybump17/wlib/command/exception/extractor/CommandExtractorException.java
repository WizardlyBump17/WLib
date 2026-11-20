package com.wizardlybump17.wlib.command.exception.extractor;

import org.jetbrains.annotations.NotNull;

public abstract class CommandExtractorException extends Exception {

    public CommandExtractorException() {
        super();
    }

    public CommandExtractorException(@NotNull String message) {
        super(message);
    }

    public CommandExtractorException(@NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
    }

    public CommandExtractorException(@NotNull Throwable cause) {
        super(cause);
    }
}
