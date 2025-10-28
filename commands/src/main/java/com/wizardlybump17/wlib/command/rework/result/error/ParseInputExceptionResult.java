package com.wizardlybump17.wlib.command.rework.result.error;

import com.wizardlybump17.wlib.command.rework.exception.InputParsingException;
import com.wizardlybump17.wlib.command.rework.node.CommandNode;
import org.jetbrains.annotations.NotNull;

public final class ParseInputExceptionResult<T> extends ExceptionResult<T> {

    public ParseInputExceptionResult(int lastInputIndex, @NotNull CommandNode<?> lastNode, @NotNull InputParsingException exception) {
        super(lastInputIndex, lastNode, exception);
    }

    @Override
    public @NotNull InputParsingException exception() {
        return (InputParsingException) super.exception();
    }
}
