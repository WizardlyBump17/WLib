package com.wizardlybump17.wlib.command.rework.node;

import com.wizardlybump17.wlib.command.rework.exception.InputParsingException;
import com.wizardlybump17.wlib.command.rework.exception.InvalidInputException;
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
    private final @Nullable CommandExecutor<T> executor;

    public CommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedInputs<T> allowedInputs, @Nullable CommandExecutor<T> executor) {
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

    public abstract @Nullable T parse(@NotNull String input) throws InputParsingException;

    public final boolean isValidInput(@Nullable T input) {
        return allowedInputs.isAllowed(input);
    }

    public final @Nullable T parseOrInvalid(@NotNull String input) throws InputParsingException, InvalidInputException {
        T parse = parse(input);
        if (!isValidInput(parse))
            throw new InvalidInputException("Invalid input " + input);
        return parse;
    }

    public @NotNull List<T> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<Object> args, @NotNull String currentInput) {
        return List.of();
    }

    public @Nullable CommandExecutor<T> getExecutor() {
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
