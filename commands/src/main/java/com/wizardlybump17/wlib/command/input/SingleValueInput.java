package com.wizardlybump17.wlib.command.input;

import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface SingleValueInput<T> extends AllowedInputs<T> {

    @NotNull T value();

    @Override
    default boolean isAllowed(@Nullable T input) {
        return value().equals(input);
    }

    @Override
    default @NotNull List<T> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
        return List.of(value());
    }
}
