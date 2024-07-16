package com.wizardlybump17.wlib.util.exception;

import lombok.NonNull;

public class QuotedStringException extends RuntimeException {

    public static final @NonNull String QUOTED_WITHOUT_DELIMITER = "Can not have quoted strings without the delimiter";
    public static final @NonNull String NON_QUOTED_AFTER_QUOTED = "Can not have non-quoted strings after quoted strings";
    public static final @NonNull String INVALID_ESCAPE = "Invalid escape sequence";
    public static final @NonNull String UNCLOSED_QUOTE = "Unclosed quote";

    public QuotedStringException(@NonNull String message) {
        super(message);
    }
}
