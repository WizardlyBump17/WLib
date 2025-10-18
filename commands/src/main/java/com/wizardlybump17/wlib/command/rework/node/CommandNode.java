package com.wizardlybump17.wlib.command.rework.node;

import com.wizardlybump17.wlib.command.rework.executor.CommandExecutor;
import com.wizardlybump17.wlib.command.rework.input.AllowedInputs;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class CommandNode<T> {

    private final @NotNull String name;
    private final @NotNull @Unmodifiable List<CommandNode<?>> children;
    private final @NotNull AllowedInputs<T> allowedInputs;
    private final @Nullable CommandExecutor executor;

    public CommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedInputs<T> allowedInputs, @Nullable CommandExecutor executor) {
        this.name = name;
        this.children = Collections.unmodifiableList(children);
        this.allowedInputs = allowedInputs;
        this.executor = executor;
    }

    public CommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedInputs<T> allowedInputs) {
        this(name, children, allowedInputs, null);
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

    public @Nullable CommandExecutor getExecutor() {
        return executor;
    }

    @Override
    public String toString() {
        return "CommandNode{" +
                "name='" + name + '\'' +
                ", children=" + children +
                ", allowedInputs=" + allowedInputs +
                ", executor=" + executor +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        CommandNode<?> that = (CommandNode<?>) o;
        return Objects.equals(name, that.name)
                && Objects.equals(children, that.children)
                && Objects.equals(allowedInputs, that.allowedInputs)
                && Objects.equals(executor, that.executor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, children, allowedInputs, executor);
    }
}
