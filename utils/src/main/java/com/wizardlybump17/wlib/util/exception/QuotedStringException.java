package com.wizardlybump17.wlib.util.exception;

import lombok.NonNull;

public class QuotedStringException extends RuntimeException {

    public static final @NonNull String INVALID_ESCAPE = "Invalid escape sequence";
    public static final @NonNull String UNCLOSED_QUOTE = "Unclosed quote";

    public QuotedStringException(@NonNull String message) {
        super(message);
    }
}
