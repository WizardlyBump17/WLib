package com.wizardlybump17.wlib.command.result.success;

import com.wizardlybump17.wlib.command.result.CommandResult;
import com.wizardlybump17.wlib.command.result.error.ErrorDetails;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record SuccessResult<T>(@Nullable T data) implements CommandResult<T> {

    public static final @NotNull String ID = "WLib:Success";

    @Override
    public boolean success() {
        return true;
    }

    @Override
    public @NotNull String id() {
        return ID;
    }

    @Override
    public @Nullable ErrorDetails errorDetails() {
        return null;
    }
}
