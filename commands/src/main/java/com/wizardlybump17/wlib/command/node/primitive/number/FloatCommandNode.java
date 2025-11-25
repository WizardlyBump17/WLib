package com.wizardlybump17.wlib.command.node.primitive.number;

import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.primitive.number.AllowedFloatInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.node.primitive.PrimitiveCommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FloatCommandNode extends NumberCommandNode<Float> implements PrimitiveCommandNode {

    public FloatCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedFloatInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, allowedInputs, executor, permission);
    }

    public FloatCommandNode(@NotNull String name, @NotNull AllowedFloatInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        this(name, List.of(), allowedInputs, executor, permission);
    }

    public FloatCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedFloatInputs allowedInputs, @Nullable String permission) {
        this(name, children, allowedInputs, null, permission);
    }

    public FloatCommandNode(@NotNull String name, @NotNull AllowedFloatInputs allowedInputs, @Nullable String permission) {
        this(name, List.of(), allowedInputs, null, permission);
    }

    public FloatCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedFloatInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor) {
        super(name, children, allowedInputs, executor, null);
    }

    public FloatCommandNode(@NotNull String name, @NotNull AllowedFloatInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor) {
        this(name, List.of(), allowedInputs, executor, null);
    }

    public FloatCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedFloatInputs allowedInputs) {
        this(name, children, allowedInputs, null, null);
    }

    public FloatCommandNode(@NotNull String name, @NotNull AllowedFloatInputs allowedInputs) {
        this(name, List.of(), allowedInputs, null, null);
    }

    @Override
    public @NotNull AllowedFloatInputs getAllowedInputs() {
        return (AllowedFloatInputs) super.getAllowedInputs();
    }

    @Override
    public @NotNull Float parse(@NotNull String input) throws InputParsingException {
        try {
            return Float.parseFloat(input);
        } catch (NumberFormatException e) {
            throw new InputParsingException("Could not parse as float: " + input, e);
        }
    }

    @Override
    public @NotNull FloatCommandNode withChildren(@NotNull List<CommandNode<?>> children) {
        return new FloatCommandNode(getName(), children, getAllowedInputs(), getExecutor(), getPermission());
    }

    @Override
    public @NotNull FloatCommandNode withExecutor(@Nullable CommandNodeExecutor<?> executor) {
        return new FloatCommandNode(getName(), getChildren(), getAllowedInputs(), executor, getPermission());
    }

    @Override
    public @NotNull FloatCommandNode withPermission(@Nullable String permission) {
        return new FloatCommandNode(getName(), getChildren(), getAllowedInputs(), getExecutor(), permission);
    }
}
