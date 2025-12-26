package com.wizardlybump17.wlib.command.extractor.method.factory.object;

import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.AllowedInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.suggestion.Suggester;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;

public class InstantCommandNode extends CommandNode<Instant> {

    public InstantCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedInputs<Instant> allowedInputs, @Nullable Suggester<Instant> suggester, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, allowedInputs, suggester, executor, permission);
    }

    @Override
    public @Nullable Instant parse(@NotNull String input) throws InputParsingException {
        try {
            return Instant.parse(input);
        } catch (DateTimeParseException e) {
            throw new InputParsingException("Could not parse as Instant: " + input, e);
        }
    }

    @Override
    public @NotNull InstantCommandNode withChildren(@NotNull List<CommandNode<?>> children) {
        return new InstantCommandNode(getName(), children, getAllowedInputs(), getSuggester(), getExecutor(), getPermission());
    }

    @Override
    public @NotNull InstantCommandNode withExecutor(@Nullable CommandNodeExecutor<?> executor) {
        return new InstantCommandNode(getName(), getChildren(), getAllowedInputs(), getSuggester(), executor, getPermission());
    }

    @Override
    public @NotNull InstantCommandNode withPermission(@Nullable String permission) {
        return new InstantCommandNode(getName(), getChildren(), getAllowedInputs(), getSuggester(), getExecutor(), permission);
    }
}
