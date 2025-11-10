package com.wizardlybump17.wlib.command.exception.extractor.method;

import com.wizardlybump17.wlib.command.exception.extractor.CommandExtractorException;
import org.jetbrains.annotations.NotNull;

public class InvalidCombinationException extends CommandExtractorException {

    public InvalidCombinationException(@NotNull String message) {
        super(message);
    }
}
