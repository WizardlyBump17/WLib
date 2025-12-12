package com.wizardlybump17.wlib.command.node.object;

import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.object.AllowedUUIDInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.suggestion.object.UUIDSuggester;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.UUID;

public class UUIDCommandNode extends CommandNode<UUID> {

    public UUIDCommandNode(@NotNull String name, @NotNull @Unmodifiable List<CommandNode<?>> children, @NotNull AllowedUUIDInputs allowedInputs, @Nullable UUIDSuggester suggester, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, allowedInputs, suggester, executor, permission);
    }

    @Override
    public @NotNull AllowedUUIDInputs getAllowedInputs() {
        return (AllowedUUIDInputs) super.getAllowedInputs();
    }

    @Override
    public @Nullable UUIDSuggester getSuggester() {
        return (UUIDSuggester) super.getSuggester();
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
        return new UUIDCommandNode(getName(), children, getAllowedInputs(), getSuggester(), getExecutor(), getPermission());
    }

    @Override
    public @NotNull UUIDCommandNode withExecutor(@Nullable CommandNodeExecutor<?> executor) {
        return new UUIDCommandNode(getName(), getChildren(), getAllowedInputs(), getSuggester(), executor, getPermission());
    }

    @Override
    public @NotNull UUIDCommandNode withPermission(@Nullable String permission) {
        return new UUIDCommandNode(getName(), getChildren(), getAllowedInputs(), getSuggester(), getExecutor(), permission);
    }
}
