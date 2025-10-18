package com.wizardlybump17.wlib.command.rework.input;

import org.jetbrains.annotations.Nullable;

public interface AllowedInputs<T> {

    boolean isAllowed(@Nullable T input);
}
