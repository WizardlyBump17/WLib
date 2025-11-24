package com.wizardlybump17.wlib.command.input;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface AllowedListInputs<T> extends AllowedInputs<T> {

    @NotNull List<T> getAllowedValues();
}
