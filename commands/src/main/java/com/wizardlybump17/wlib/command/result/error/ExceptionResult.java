package com.wizardlybump17.wlib.command.result.error;

import org.jetbrains.annotations.NotNull;

public record ExceptionResult<T>(@NotNull Throwable exception, @NotNull ErrorDetails errorDetails) implements UnsuccessResult<T> {

    public static final @NotNull String ID = "WLib:Unsuccess/Exception";

    @Override
    public @NotNull String id() {
        return ID;
    }
}
