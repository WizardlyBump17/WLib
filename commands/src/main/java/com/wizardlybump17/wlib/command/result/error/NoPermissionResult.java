package com.wizardlybump17.wlib.command.result.error;

import com.wizardlybump17.wlib.command.node.CommandNode;
import org.jetbrains.annotations.NotNull;

/**
 * @deprecated use {@link UnauthorizedResult} or {@link ForbiddenResult}
 */
@Deprecated
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
