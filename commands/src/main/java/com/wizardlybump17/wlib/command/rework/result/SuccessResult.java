package com.wizardlybump17.wlib.command.rework.result;

import org.jetbrains.annotations.NotNull;

public final class SuccessResult implements CommandResult {

    public static final @NotNull SuccessResult INSTANCE = new SuccessResult();

    private SuccessResult() {
    }

    @Override
    public boolean success() {
        return false;
    }
}
