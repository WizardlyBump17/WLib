package com.wizardlybump17.wlib.command.node.primitive.number;

import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.AllowedInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.node.primitive.PrimitiveCommandNode;
import com.wizardlybump17.wlib.command.suggestion.Suggester;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FloatCommandNode extends NumberCommandNode<Float> implements PrimitiveCommandNode {

    public FloatCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedInputs<Float> allowedInputs, @Nullable Suggester<Float> suggester, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, allowedInputs, suggester, executor, permission);
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
        return new FloatCommandNode(getName(), children, getAllowedInputs(), getSuggester(), getExecutor(), getPermission());
    }

    @Override
    public @NotNull FloatCommandNode withExecutor(@Nullable CommandNodeExecutor<?> executor) {
        return new FloatCommandNode(getName(), getChildren(), getAllowedInputs(), getSuggester(), executor, getPermission());
    }

    @Override
    public @NotNull FloatCommandNode withPermission(@Nullable String permission) {
        return new FloatCommandNode(getName(), getChildren(), getAllowedInputs(), getSuggester(), getExecutor(), permission);
    }
}
