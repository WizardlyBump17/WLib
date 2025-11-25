package com.wizardlybump17.wlib.command.node.object;

import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.object.AllowedUUIDInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class UUIDCommandNode extends CommandNode<UUID> {

    public UUIDCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedUUIDInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, allowedInputs, executor, permission);
    }

    public UUIDCommandNode(@NotNull String name, @NotNull AllowedUUIDInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, allowedInputs, executor, permission);
    }

    public UUIDCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedUUIDInputs allowedInputs, @Nullable String permission) {
        super(name, children, allowedInputs, permission);
    }

    public UUIDCommandNode(@NotNull String name, @NotNull AllowedUUIDInputs allowedInputs, @Nullable String permission) {
        super(name, allowedInputs, permission);
    }

    public UUIDCommandNode(@NotNull String name, @NotNull AllowedUUIDInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor) {
        super(name, allowedInputs, executor);
    }

    public UUIDCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedUUIDInputs allowedInputs) {
        super(name, children, allowedInputs);
    }

    public UUIDCommandNode(@NotNull String name, @NotNull AllowedUUIDInputs allowedInputs) {
        super(name, allowedInputs);
    }

    @Override
    public @NotNull AllowedUUIDInputs getAllowedInputs() {
        return (AllowedUUIDInputs) super.getAllowedInputs();
    }

    @Override
    public @Nullable UUID parse(@NotNull String input) throws InputParsingException {
        try {
            return UUID.fromString(input);
        } catch (IllegalArgumentException e) {
            throw new InputParsingException("Could not parse as UUID: " + input, e);
        }
    }

    @Override
    public @NotNull UUIDCommandNode withChildren(@NotNull List<CommandNode<?>> children) {
        return new UUIDCommandNode(getName(), children, getAllowedInputs(), getExecutor(), getPermission());
    }

    @Override
    public @NotNull UUIDCommandNode withExecutor(@Nullable CommandNodeExecutor<?> executor) {
        return new UUIDCommandNode(getName(), getChildren(), getAllowedInputs(), executor, getPermission());
    }

    @Override
    public @NotNull UUIDCommandNode withPermission(@Nullable String permission) {
        return new UUIDCommandNode(getName(), getChildren(), getAllowedInputs(), getExecutor(), permission);
    }
}
