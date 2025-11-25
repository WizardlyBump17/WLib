package com.wizardlybump17.wlib.command.node.primitive.number;

import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.primitive.number.AllowedShortInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShortCommandNode extends CommandNode<Short> {

    public ShortCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedShortInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, allowedInputs, executor, permission);
    }

    public ShortCommandNode(@NotNull String name, @NotNull AllowedShortInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        this(name, List.of(), allowedInputs, executor, permission);
    }

    public ShortCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedShortInputs allowedInputs, @Nullable String permission) {
        this(name, children, allowedInputs, null, permission);
    }

    public ShortCommandNode(@NotNull String name, @NotNull AllowedShortInputs allowedInputs, @Nullable String permission) {
        this(name, List.of(), allowedInputs, null, permission);
    }

    public ShortCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedShortInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor) {
        super(name, children, allowedInputs, executor, null);
    }

    public ShortCommandNode(@NotNull String name, @NotNull AllowedShortInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor) {
        this(name, List.of(), allowedInputs, executor, null);
    }

    public ShortCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedShortInputs allowedInputs) {
        this(name, children, allowedInputs, null, null);
    }

    public ShortCommandNode(@NotNull String name, @NotNull AllowedShortInputs allowedInputs) {
        this(name, List.of(), allowedInputs, null, null);
    }

    @Override
    public @NotNull AllowedShortInputs getAllowedInputs() {
        return (AllowedShortInputs) super.getAllowedInputs();
    }

    @Override
    public @NotNull Short parse(@NotNull String input) throws InputParsingException {
        try {
            return Short.parseShort(input);
        } catch (NumberFormatException e) {
            throw new InputParsingException("Could not parse as short: " + input, e);
        }
    }

    @Override
    public @NotNull ShortCommandNode withChildren(@NotNull List<CommandNode<?>> children) {
        return new ShortCommandNode(getName(), children, getAllowedInputs(), getExecutor(), getPermission());
    }

    @Override
    public @NotNull ShortCommandNode withExecutor(@Nullable CommandNodeExecutor<?> executor) {
        return new ShortCommandNode(getName(), getChildren(), getAllowedInputs(), executor, getPermission());
    }

    @Override
    public @NotNull ShortCommandNode withPermission(@Nullable String permission) {
        return new ShortCommandNode(getName(), getChildren(), getAllowedInputs(), getExecutor(), permission);
    }
}
