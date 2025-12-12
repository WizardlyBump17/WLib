package com.wizardlybump17.wlib.command.node.primitive;

import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.primitive.AllowedBooleanInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.suggestion.primitive.BooleanSuggester;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public class BooleanCommandNode extends AbstractPrimitiveCommandNode<Boolean> {

    public BooleanCommandNode(@NotNull String name, @NotNull @Unmodifiable List<CommandNode<?>> children, @NotNull AllowedBooleanInputs allowedInputs, @Nullable BooleanSuggester suggester, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, allowedInputs, suggester, executor, permission);
    }

    @Override
    public @NotNull AllowedBooleanInputs getAllowedInputs() {
        return (AllowedBooleanInputs) super.getAllowedInputs();
    }

    @Override
    public @Nullable BooleanSuggester getSuggester() {
        return (BooleanSuggester) super.getSuggester();
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
        return new BooleanCommandNode(getName(), children, getAllowedInputs(), getSuggester(), getExecutor(), getPermission());
    }

    @Override
    public @NotNull BooleanCommandNode withExecutor(@Nullable CommandNodeExecutor<?> executor) {
        return new BooleanCommandNode(getName(), getChildren(), getAllowedInputs(), getSuggester(), executor, getPermission());
    }

    @Override
    public @NotNull BooleanCommandNode withPermission(@Nullable String permission) {
        return new BooleanCommandNode(getName(), getChildren(), getAllowedInputs(), getSuggester(), getExecutor(), permission);
    }
}
