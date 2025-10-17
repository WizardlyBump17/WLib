package com.wizardlybump17.wlib.command.rework.node;

import com.wizardlybump17.wlib.command.rework.node.input.AllowedInputs;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;

public abstract class CommandNode<T> {

    private final @NotNull String name;
    private final @NotNull @Unmodifiable List<CommandNode<?>> children;
    private final @NotNull AllowedInputs<T> allowedInputs;
    private @NotNull CommandNode<?> parent = this;

    public CommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedInputs<T> allowedInputs) {
        this.name = name;

        children.forEach(child -> child.setParent(this));
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

    public abstract @NotNull ParseResult<T> parse(@NotNull String input);

    public final boolean isValidInput(@Nullable T input) {
        return allowedInputs.isAllowed(input);
    }

    public @NotNull List<T> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<Object> args, @NotNull String currentInput) {
        return List.of();
    }

    public @NotNull CommandNode<?> getParent() {
        return parent;
    }

    public void setParent(@NotNull CommandNode<?> parent) {
        this.parent = parent;
    }

    public boolean hasParent() {
        return parent != this;
    }

    public record ParseResult<T>(boolean success, @Nullable T value) {

        public static <T> @NotNull ParseResult<T> success(@Nullable T value) {
            return new ParseResult<>(true, value);
        }

        public static <T> @NotNull ParseResult<T> emptySuccess() {
            return new ParseResult<>(true, null);
        }

        public static <T> @NotNull ParseResult<T> failure() {
            return new ParseResult<>(false, null);
        }
    }
}
