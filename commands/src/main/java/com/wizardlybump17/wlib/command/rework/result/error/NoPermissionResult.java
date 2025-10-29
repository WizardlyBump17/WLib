package com.wizardlybump17.wlib.command.rework.result.error;

import com.wizardlybump17.wlib.command.rework.node.CommandNode;
import org.jetbrains.annotations.NotNull;

public record NoPermissionResult<T>(int lastInputIndex, @NotNull CommandNode<?> lastNode) implements UnsuccessResult<T> {

    public @NotNull String permission() {
        return lastNode.getPermission();
    }
}
