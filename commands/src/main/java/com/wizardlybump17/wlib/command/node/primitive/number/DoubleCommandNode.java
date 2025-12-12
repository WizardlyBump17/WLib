package com.wizardlybump17.wlib.command.node.primitive.number;

import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.primitive.number.AllowedDoubleInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.node.primitive.PrimitiveCommandNode;
import com.wizardlybump17.wlib.command.suggestion.primitive.number.DoubleSuggester;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DoubleCommandNode extends NumberCommandNode<Double> implements PrimitiveCommandNode {

    public DoubleCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedDoubleInputs allowedInputs, @Nullable DoubleSuggester suggester, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, allowedInputs, suggester, executor, permission);
    }

    @Override
    public @NotNull AllowedDoubleInputs getAllowedInputs() {
        return (AllowedDoubleInputs) super.getAllowedInputs();
    }

    @Override
    public @Nullable DoubleSuggester getSuggester() {
        return (DoubleSuggester) super.getSuggester();
    }

    @Override
    public @NotNull Double parse(@NotNull String input) throws InputParsingException {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            throw new InputParsingException("Could not parse as double: " + input, e);
        }
    }

    @Override
    public @NotNull DoubleCommandNode withChildren(@NotNull List<CommandNode<?>> children) {
        return new DoubleCommandNode(getName(), children, getAllowedInputs(), getSuggester(), getExecutor(), getPermission());
    }

    @Override
    public @NotNull DoubleCommandNode withExecutor(@Nullable CommandNodeExecutor<?> executor) {
        return new DoubleCommandNode(getName(), getChildren(), getAllowedInputs(), getSuggester(), executor, getPermission());
    }

    @Override
    public @NotNull DoubleCommandNode withPermission(@Nullable String permission) {
        return new DoubleCommandNode(getName(), getChildren(), getAllowedInputs(), getSuggester(), getExecutor(), permission);
    }
}
