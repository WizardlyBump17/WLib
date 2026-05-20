package com.wizardlybump17.wlib.command.result.error;

import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.node.CommandNode;
import org.jetbrains.annotations.NotNull;

public record ParseInputExceptionResult<T>(int lastInputIndex, @NotNull CommandNode<?> lastNode, @NotNull InputParsingException exception, @NotNull ErrorDetails errorDetails) implements UnsuccessResult<T> {

    public static final @NotNull String ID = "WLib:Unsuccess/ParseError";

    @Override
    public @NotNull String id() {
        return ID;
    }
}
