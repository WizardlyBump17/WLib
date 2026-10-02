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

public class LongCommandNode extends NumberCommandNode<Long> implements PrimitiveCommandNode {

    public LongCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedInputs<Long> allowedInputs, @Nullable Suggester<Long> suggester, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, allowedInputs, suggester, executor, permission);
    }

    @Override
    public @NotNull Long parse(@NotNull String input) throws InputParsingException {
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            throw new InputParsingException("Could not parse as short: " + input, e);
        }
    }

    @Override
    public @NotNull LongCommandNode withChildren(@NotNull List<CommandNode<?>> children) {
        return new LongCommandNode(getName(), children, getAllowedInputs(), getSuggester(), getExecutor(), getPermission());
    }

    @Override
    public @NotNull LongCommandNode withExecutor(@Nullable CommandNodeExecutor<?> executor) {
        return new LongCommandNode(getName(), getChildren(), getAllowedInputs(), getSuggester(), executor, getPermission());
    }

    @Override
    public @NotNull LongCommandNode withPermission(@Nullable String permission) {
        return new LongCommandNode(getName(), getChildren(), getAllowedInputs(), getSuggester(), getExecutor(), permission);
    }
}
