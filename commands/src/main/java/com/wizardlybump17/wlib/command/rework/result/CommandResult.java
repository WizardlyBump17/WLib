package com.wizardlybump17.wlib.command.rework.result;

import org.jetbrains.annotations.NotNull;

public interface CommandResult {

    boolean success();

    static @NotNull SuccessResult successful() {
        return SuccessResult.INSTANCE;
    }

    static @NotNull ExceptionResult exceptionally(@NotNull Throwable throwable) {
        return new ExceptionResult(throwable);
    }

    static @NotNull GenericErrorResult error(@NotNull String message) {
        return new GenericErrorResult(message);
    }

    static @NotNull GenericErrorResult error() {
        return GenericErrorResult.DEFAULT_ERROR;
    }
}
