package com.wizardlybump17.wlib.command.rework.result;

import org.jetbrains.annotations.NotNull;

public record GenericErrorResult(@NotNull String message) implements CommandResult {

    public static final @NotNull String DEFAULT_MESSAGE = "An error occurred while executing the command";
    public static final @NotNull GenericErrorResult DEFAULT_ERROR = new GenericErrorResult(DEFAULT_MESSAGE);

    @Override
    public boolean success() {
        return false;
    }
}
