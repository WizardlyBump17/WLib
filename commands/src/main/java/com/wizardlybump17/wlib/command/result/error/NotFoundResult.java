package com.wizardlybump17.wlib.command.result.error;

import org.jetbrains.annotations.NotNull;

public record NotFoundResult<T>(@NotNull ErrorDetails errorDetails) implements UnsuccessResult<T> {

    public static final @NotNull String ID = "WLib:Error/NotFound";

    @Override
    public @NotNull String id() {
        return ID;
    }
}
