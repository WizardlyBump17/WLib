package com.wizardlybump17.wlib.command.input;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SingleValueInput<T> extends AllowedInputs<T> {

    @NotNull T value();

    @Override
    default boolean isAllowed(@Nullable T input) {
        return value().equals(input);
    }
}
