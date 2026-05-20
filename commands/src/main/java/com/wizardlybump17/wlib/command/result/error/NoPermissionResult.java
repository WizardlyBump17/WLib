package com.wizardlybump17.wlib.command.result.error;

import org.jetbrains.annotations.NotNull;

/**
 * @deprecated use {@link UnauthorizedResult} or {@link ForbiddenResult}
 */
@Deprecated
public record NoPermissionResult<T>(@NotNull ErrorDetails errorDetails) implements UnsuccessResult<T> {

    public static final @NotNull String ID = "WLib:Unsuccess/NoPermission";

    @Override
    public @NotNull String id() {
        return ID;
    }
}
