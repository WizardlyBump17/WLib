package com.wizardlybump17.wlib.command.node;

import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.AllowedNumberInputs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IntegerCommandNode extends CommandNode<Integer> {

    public IntegerCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedNumberInputs.AllowedIntegerInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, allowedInputs, executor, permission);
    }

    public IntegerCommandNode(@NotNull String name, @NotNull AllowedNumberInputs.AllowedIntegerInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        this(name, List.of(), allowedInputs, executor, permission);
    }

    public IntegerCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedNumberInputs.AllowedIntegerInputs allowedInputs, @Nullable String permission) {
        this(name, children, allowedInputs, null, permission);
    }

    public IntegerCommandNode(@NotNull String name, @NotNull AllowedNumberInputs.AllowedIntegerInputs allowedInputs, @Nullable String permission) {
        this(name, List.of(), allowedInputs, null, permission);
    }

    public IntegerCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedNumberInputs.AllowedIntegerInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor) {
        super(name, children, allowedInputs, executor, null);
    }

    public IntegerCommandNode(@NotNull String name, @NotNull AllowedNumberInputs.AllowedIntegerInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor) {
        this(name, List.of(), allowedInputs, executor, null);
    }

    public IntegerCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedNumberInputs.AllowedIntegerInputs allowedInputs) {
        this(name, children, allowedInputs, null, null);
    }

    public IntegerCommandNode(@NotNull String name, @NotNull AllowedNumberInputs.AllowedIntegerInputs allowedInputs) {
        this(name, List.of(), allowedInputs, null, null);
    }

    @Override
    public @NotNull AllowedNumberInputs.AllowedIntegerInputs getAllowedInputs() {
        return (AllowedNumberInputs.AllowedIntegerInputs) super.getAllowedInputs();
    }

    @Override
    public @NotNull Integer parse(@NotNull String input) throws InputParsingException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new InputParsingException("Could not parse as int: " + input, e);
        }
    }

    @Override
    public @NotNull IntegerCommandNode withChildren(@NotNull List<CommandNode<?>> children) {
        return new IntegerCommandNode(getName(), children, getAllowedInputs(), getExecutor(), getPermission());
    }

    @Override
    public @NotNull IntegerCommandNode withExecutor(@Nullable CommandNodeExecutor<?> executor) {
        return new IntegerCommandNode(getName(), getChildren(), getAllowedInputs(), executor, getPermission());
    }

    @Override
    public @NotNull CommandNode<Integer> withPermission(@Nullable String permission) {
        return new IntegerCommandNode(getName(), getChildren(), getAllowedInputs(), getExecutor(), permission);
    }
}
