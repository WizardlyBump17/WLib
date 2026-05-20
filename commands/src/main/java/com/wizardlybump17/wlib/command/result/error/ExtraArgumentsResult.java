package com.wizardlybump17.wlib.command.result.error;

import org.jetbrains.annotations.NotNull;

public record ExtraArgumentsResult<T>(@NotNull ErrorDetails errorDetails) implements UnsuccessResult<T> {

    public static final @NotNull String ID = "WLib:Unsuccess/ExtraArguments";

    @Override
    public @NotNull String id() {
        return ID;
    }
}
