package com.wizardlybump17.wlib.command.rework.input;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record LiteralAllowedInput(@NotNull String value, boolean caseSensitive) implements AllowedInputs<String> {

    @Override
    public boolean isAllowed(@Nullable String input) {
        if (input == null)
            return false;
        if (caseSensitive)
            return input.equals(value);
        return input.equalsIgnoreCase(value);
    }
}
