package com.wizardlybump17.wlib.command.exception.extractor.method;

import com.wizardlybump17.wlib.command.exception.extractor.CommandExtractorException;
import org.jetbrains.annotations.NotNull;

public class MethodCommandNodeFactoryNotFoundException extends CommandExtractorException {

    public MethodCommandNodeFactoryNotFoundException() {
        super();
    }

    public MethodCommandNodeFactoryNotFoundException(@NotNull String message) {
        super(message);
    }
}
