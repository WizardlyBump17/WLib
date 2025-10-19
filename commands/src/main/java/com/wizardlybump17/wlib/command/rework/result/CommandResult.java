package com.wizardlybump17.wlib.command.rework.result;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CommandResult<T> {

    boolean success();

    @Nullable T data();

    static <T> @NotNull SuccessResult<T> successful(@Nullable T data) {
        return new SuccessResult<>(data);
    }

    static <T> @NotNull ExceptionResult<T> exceptionally(@NotNull Throwable throwable) {
        return new ExceptionResult<>(throwable);
    }

    static <T> @NotNull GenericErrorResult<T> error(@NotNull String message) {
        return new GenericErrorResult<>(message);
    }

    static <T> @NotNull GenericErrorResult<T> error() {
        return new GenericErrorResult<>();
    }
}
