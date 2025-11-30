package com.wizardlybump17.wlib.command.extractor;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.exception.extractor.CommandExtractorException;
import com.wizardlybump17.wlib.command.extractor.method.MethodCommandExtractor;
import com.wizardlybump17.wlib.command.registry.MethodCommandNodeFactoryRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CommandExtractor {

    boolean isAccepted(@NotNull Object object);

    @NotNull List<Command> extract(@NotNull Object object) throws CommandExtractorException;

    static @NotNull MethodCommandExtractor method(@NotNull MethodCommandNodeFactoryRegistry factoryRegistry) {
        return new MethodCommandExtractor(factoryRegistry);
    }
}
