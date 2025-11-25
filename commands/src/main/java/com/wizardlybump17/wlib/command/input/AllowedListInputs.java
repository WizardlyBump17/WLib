package com.wizardlybump17.wlib.command.input;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface AllowedListInputs<T> extends AllowedInputs<T> {

    @NotNull List<T> allowedValues();

    @Override
    default boolean isAllowed(@Nullable T input) {
        if (input == null)
            return false;
        return allowedValues().contains(input);
    }
}
