package com.wizardlybump17.wlib.command.result.empty;

import com.wizardlybump17.wlib.command.result.CommandResult;
import org.jetbrains.annotations.NotNull;

public sealed interface EmptyResult<T> extends CommandResult<T> permits EmptySuccess, EmptyError {

    @Override
    default int lastInputIndex() {
        return -1;
    }

    @SuppressWarnings("unchecked")
    static <T> @NotNull EmptyResult<T> emptySuccess() {
        return (EmptyResult<T>) EmptySuccess.INSTANCE;
    }

    @SuppressWarnings("unchecked")
    static <T> @NotNull EmptyResult<T> emptyError() {
        return (EmptyResult<T>) EmptyError.INSTANCE;
    }
}
