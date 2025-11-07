package com.wizardlybump17.wlib.command.result.error;

import com.wizardlybump17.wlib.command.node.CommandNode;
import org.jetbrains.annotations.NotNull;

public record OutOfRangeInputResult<T>(int lastInputIndex, @NotNull CommandNode<?> lastNode) implements UnsuccessResult<T> {
}
