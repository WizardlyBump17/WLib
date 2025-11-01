package com.wizardlybump17.wlib.command.rework.input;

import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface AllowedInputs<T> {

    boolean isAllowed(@Nullable T input);

    default @NotNull List<T> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
        return List.of();
    }
}
