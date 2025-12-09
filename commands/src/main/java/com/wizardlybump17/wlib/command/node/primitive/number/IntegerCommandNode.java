package com.wizardlybump17.wlib.command.node.primitive.number;

import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.AllowedInputs;
import com.wizardlybump17.wlib.command.input.primitive.number.AllowedIntegerInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.node.primitive.PrimitiveCommandNode;
import com.wizardlybump17.wlib.command.suggestion.Suggester;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IntegerCommandNode extends NumberCommandNode<Integer> implements PrimitiveCommandNode {

    public IntegerCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedIntegerInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, allowedInputs, executor, permission);
    }

    public IntegerCommandNode(@NotNull String name, @NotNull AllowedIntegerInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        this(name, List.of(), allowedInputs, executor, permission);
    }

    public IntegerCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedIntegerInputs allowedInputs, @Nullable String permission) {
        this(name, children, allowedInputs, null, permission);
    }

    public IntegerCommandNode(@NotNull String name, @NotNull AllowedIntegerInputs allowedInputs, @Nullable String permission) {
        this(name, List.of(), allowedInputs, null, permission);
    }

    public IntegerCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedIntegerInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor) {
        super(name, children, allowedInputs, executor, null);
    }

    public IntegerCommandNode(@NotNull String name, @NotNull AllowedIntegerInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor) {
        this(name, List.of(), allowedInputs, executor, null);
    }

    public IntegerCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedIntegerInputs allowedInputs) {
        this(name, children, allowedInputs, null, null);
    }

    public IntegerCommandNode(@NotNull String name, @NotNull AllowedIntegerInputs allowedInputs) {
        this(name, List.of(), allowedInputs, null, null);
    }

    public IntegerCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedInputs<Integer> allowedInputs, @Nullable Suggester<Integer> suggester, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, allowedInputs, suggester, executor, permission);
    }

    @Override
    public @NotNull AllowedIntegerInputs getAllowedInputs() {
        return (AllowedIntegerInputs) super.getAllowedInputs();
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
    public @NotNull IntegerCommandNode withPermission(@Nullable String permission) {
        return new IntegerCommandNode(getName(), getChildren(), getAllowedInputs(), getExecutor(), permission);
    }
}
