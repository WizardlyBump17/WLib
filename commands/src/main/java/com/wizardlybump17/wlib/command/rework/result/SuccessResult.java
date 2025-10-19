package com.wizardlybump17.wlib.command.rework.result;

import org.jetbrains.annotations.Nullable;

public record SuccessResult<T>(@Nullable T data) implements CommandResult<T> {

    @Override
    public boolean success() {
        return false;
    }
}
