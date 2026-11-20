package com.wizardlybump17.wlib.command.input;

import org.jetbrains.annotations.NotNull;

public interface RangedAllowedInputs<T> extends AllowedInputs<T> {

    @NotNull T from();

    @NotNull T to();

    boolean isInRange(@NotNull T input);
}
