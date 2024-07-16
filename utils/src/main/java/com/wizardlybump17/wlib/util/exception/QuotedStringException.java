package com.wizardlybump17.wlib.util.exception;

import lombok.NonNull;

public class QuotedStringException extends RuntimeException {

    public QuotedStringException(@NonNull String message) {
        super(message);
    }
}
