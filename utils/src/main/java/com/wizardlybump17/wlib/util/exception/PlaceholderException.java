package com.wizardlybump17.wlib.util.exception;

import lombok.NonNull;

public class PlaceholderException extends RuntimeException {

    public PlaceholderException(@NonNull String message) {
        super(message);
    }
}
