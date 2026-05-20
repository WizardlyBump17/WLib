package com.wizardlybump17.wlib.command.result.error;

import org.jetbrains.annotations.NotNull;

public record BadRequestResult<T>(@NotNull ErrorDetails errorDetails) implements UnsuccessResult<T> {

    public static final @NotNull String ID = "WLib:Error/BadRequest";

    @Override
    public @NotNull String id() {
        return ID;
    }
}
