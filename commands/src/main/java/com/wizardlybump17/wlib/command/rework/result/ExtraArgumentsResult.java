package com.wizardlybump17.wlib.command.rework.result;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public record ExtraArgumentsResult<T>(int index) implements CommandResult<T> {

    @Override
    public boolean success() {
        return false;
    }

    @Contract("-> null")
    @Override
    public @Nullable T data() {
        return null;
    }
}
