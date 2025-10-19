package com.wizardlybump17.wlib.command.rework.result;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ExceptionResult<T>(@NotNull Throwable exception) implements CommandResult<T> {

    @Override
    public boolean success() {
        return false;
    }

    @Override
    public @Nullable T data() {
        return null;
    }
}
