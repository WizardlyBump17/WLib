package com.wizardlybump17.wlib.command.extractor;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.exception.extractor.CommandExtractorException;
import com.wizardlybump17.wlib.command.extractor.method.MethodCommandExtractor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CommandExtractor {

    @NotNull MethodCommandExtractor METHOD = MethodCommandExtractor.INSTANCE;

    boolean isAccepted(@NotNull Object object);

    @NotNull List<Command> extract(@NotNull Object object) throws CommandExtractorException;
}
