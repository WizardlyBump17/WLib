package com.wizardlybump17.wlib.command.result.error;

import org.jetbrains.annotations.NotNull;

public record ErrorDetails(@NotNull String code, @NotNull String message, @NotNull String detail) {
}
