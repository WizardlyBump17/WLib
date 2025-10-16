package com.wizardlybump17.wlib.command.rework.node;

import com.wizardlybump17.wlib.command.rework.node.input.AllowedInputs;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class CommandNode<T> {

    private final @NotNull String name;
    private final @NotNull @Unmodifiable List<CommandNode<?>> children;
    private final @NotNull AllowedInputs<T> allowedInputs;

    public CommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedInputs<T> allowedInputs) {
        this.name = name;
        this.children = Collections.unmodifiableList(children);
        this.allowedInputs = allowedInputs;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull @Unmodifiable List<CommandNode<?>> getChildren() {
        return children;
    }

    public @NotNull AllowedInputs<T> getAllowedInputs() {
        return allowedInputs;
    }

    public abstract @NotNull Optional<T> parse(@NotNull String input);

    public @NotNull List<T> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<Object> args, @NotNull String currentInput) {
        return List.of();
    }
}
