package com.wizardlybump17.wlib.command.result.error;

import org.jetbrains.annotations.NotNull;

public record ForbiddenResult<T>(@NotNull ErrorDetails errorDetails) implements UnsuccessResult<T> {

    public static final @NotNull String ID = "WLib:Error/Forbidden";

    public static final @NotNull String NODE_NO_PERMISSION = "WLib:Forbidden/NodeNoPermission";

    @Override
    public @NotNull String id() {
        return ID;
    }
}
