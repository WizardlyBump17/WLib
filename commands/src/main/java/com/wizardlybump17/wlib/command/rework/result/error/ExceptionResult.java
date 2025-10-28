package com.wizardlybump17.wlib.command.rework.result.error;

import com.wizardlybump17.wlib.command.rework.node.CommandNode;
import org.jetbrains.annotations.NotNull;

public record ExceptionResult<T>(int lastInputIndex, @NotNull CommandNode<?> lastNode, @NotNull Throwable exception) implements UnsuccessResult<T> {
}
