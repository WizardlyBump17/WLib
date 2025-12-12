package com.wizardlybump17.wlib.command.node.string;

import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.string.AllowedStringInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.suggestion.string.StringSuggester;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public class StringCommandNode extends CommandNode<String> {

    public StringCommandNode(@NotNull String name, @NotNull @Unmodifiable List<CommandNode<?>> children, @NotNull AllowedStringInputs allowedInputs, @Nullable StringSuggester suggester, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, allowedInputs, suggester, executor, permission);
    }

    @Override
    public @NotNull AllowedStringInputs getAllowedInputs() {
        return (AllowedStringInputs) super.getAllowedInputs();
    }

    @Override
    public @Nullable StringSuggester getSuggester() {
        return (StringSuggester) super.getSuggester();
    }

    @Override
    public @Nullable String parse(@NotNull String input) throws InputParsingException {
        return input;
    }

    @Override
    public @NotNull StringCommandNode withChildren(@NotNull List<CommandNode<?>> children) {
        return new StringCommandNode(getName(), children, getAllowedInputs(), getSuggester(), getExecutor(), getPermission());
    }

    @Override
    public @NotNull StringCommandNode withExecutor(@Nullable CommandNodeExecutor<?> executor) {
        return new StringCommandNode(getName(), getChildren(), getAllowedInputs(), getSuggester(), executor, getPermission());
    }

    @Override
    public @NotNull StringCommandNode withPermission(@Nullable String permission) {
        return new StringCommandNode(getName(), getChildren(), getAllowedInputs(), getSuggester(), getExecutor(), permission);
    }
}
