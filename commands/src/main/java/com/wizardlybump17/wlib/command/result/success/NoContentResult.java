package com.wizardlybump17.wlib.command.result.success;

import com.wizardlybump17.wlib.command.result.CommandResult;
import com.wizardlybump17.wlib.command.result.error.ErrorDetails;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record NoContentResult<T>(@NotNull ErrorDetails errorDetails) implements CommandResult<T> {

    private static final @NotNull String ID = "WLib:Success/NoContent";

    @Override
    public @NotNull String id() {
        return ID;
    }

    @Override
    public boolean success() {
        return true;
    }

    @Override
    public @Nullable T data() {
        return null;
    }
}
