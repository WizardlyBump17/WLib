package com.wizardlybump17.wlib.command.result.error;

import com.wizardlybump17.wlib.command.node.CommandNode;
import org.jetbrains.annotations.NotNull;

public record NoPermissionResult<T>(int lastInputIndex, @NotNull CommandNode<?> lastNode) implements UnsuccessResult<T> {

    public static final @NotNull String ID = "WLib:Unsuccess/NoPermission";

    public @NotNull String permission() {
        return lastNode.getPermission();
    }

    @Override
    public @NotNull String id() {
        return ID;
    }
}
