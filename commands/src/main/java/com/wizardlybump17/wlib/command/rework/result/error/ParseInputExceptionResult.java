package com.wizardlybump17.wlib.command.rework.result.error;

import com.wizardlybump17.wlib.command.rework.exception.InputParsingException;
import com.wizardlybump17.wlib.command.rework.node.CommandNode;
import org.jetbrains.annotations.NotNull;

public record ParseInputExceptionResult<T>(int lastInputIndex, @NotNull CommandNode<?> lastNode, @NotNull InputParsingException exception) implements UnsuccessResult<T> {
}
