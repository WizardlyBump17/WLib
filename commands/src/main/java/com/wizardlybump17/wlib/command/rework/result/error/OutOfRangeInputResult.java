package com.wizardlybump17.wlib.command.rework.result.error;

import com.wizardlybump17.wlib.command.rework.node.CommandNode;
import org.jetbrains.annotations.NotNull;

public final class OutOfRangeInputResult<T> extends AbstractUnsuccessResult<T> {

    public OutOfRangeInputResult(int lastInputIndex, @NotNull CommandNode<?> lastNode) {
        super(lastInputIndex, lastNode);
    }
}
