package com.wizardlybump17.wlib.command.node.primitive;

import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.primitive.BooleanAllowedInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BooleanCommandNode extends CommandNode<Boolean> {

    public BooleanCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull BooleanAllowedInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, allowedInputs, executor, permission);
    }

    public BooleanCommandNode(@NotNull String name, @NotNull BooleanAllowedInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, allowedInputs, executor, permission);
    }

    public BooleanCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull BooleanAllowedInputs allowedInputs, @Nullable String permission) {
        super(name, children, allowedInputs, permission);
    }

    public BooleanCommandNode(@NotNull String name, @NotNull BooleanAllowedInputs allowedInputs, @Nullable String permission) {
        super(name, allowedInputs, permission);
    }

    public BooleanCommandNode(@NotNull String name, @NotNull BooleanAllowedInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor) {
        super(name, allowedInputs, executor);
    }

    public BooleanCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull BooleanAllowedInputs allowedInputs) {
        super(name, children, allowedInputs);
    }

    public BooleanCommandNode(@NotNull String name, @NotNull BooleanAllowedInputs allowedInputs) {
        super(name, allowedInputs);
    }

    @Override
    public @NotNull BooleanAllowedInputs getAllowedInputs() {
        return (BooleanAllowedInputs) super.getAllowedInputs();
    }

    @Override
    public @Nullable Boolean parse(@NotNull String input) throws InputParsingException {
        if (input.equalsIgnoreCase("true"))
            return true;
        if (input.equalsIgnoreCase("false"))
            return false;
        throw new InputParsingException("Could not parse as boolean: " + input);
    }

    @Override
    public @NotNull BooleanCommandNode withChildren(@NotNull List<CommandNode<?>> children) {
        return new BooleanCommandNode(getName(), children, getAllowedInputs(), getExecutor(), getPermission());
    }

    @Override
    public @NotNull BooleanCommandNode withExecutor(@Nullable CommandNodeExecutor<?> executor) {
        return new BooleanCommandNode(getName(), getChildren(), getAllowedInputs(), executor, getPermission());
    }

    @Override
    public @NotNull BooleanCommandNode withPermission(@Nullable String permission) {
        return new BooleanCommandNode(getName(), getChildren(), getAllowedInputs(), getExecutor(), permission);
    }
}
