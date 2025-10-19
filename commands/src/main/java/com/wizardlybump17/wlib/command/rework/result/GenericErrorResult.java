package com.wizardlybump17.wlib.command.rework.result;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record GenericErrorResult<T>(@NotNull String message) implements CommandResult<T> {

    public static final @NotNull String DEFAULT_MESSAGE = "An error occurred while executing the command";

    public GenericErrorResult() {
        this(DEFAULT_MESSAGE);
    }

    @Override
    public boolean success() {
        return false;
    }

    @Override
    public @Nullable T data() {
        return null;
    }
}
