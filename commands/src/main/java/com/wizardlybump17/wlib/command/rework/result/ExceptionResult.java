package com.wizardlybump17.wlib.command.rework.result;

import org.jetbrains.annotations.NotNull;

public record ExceptionResult(@NotNull Throwable exception) implements CommandResult {

    @Override
    public boolean success() {
        return false;
    }
}
