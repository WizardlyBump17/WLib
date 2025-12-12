package com.wizardlybump17.wlib.command.node.primitive.number;

import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.AllowedInputs;
import com.wizardlybump17.wlib.command.input.primitive.number.AllowedShortInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.node.primitive.PrimitiveCommandNode;
import com.wizardlybump17.wlib.command.suggestion.Suggester;
import com.wizardlybump17.wlib.command.suggestion.primitive.number.ShortSuggester;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShortCommandNode extends NumberCommandNode<Short> implements PrimitiveCommandNode {

    public ShortCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedInputs<Short> allowedInputs, @Nullable Suggester<Short> suggester, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, allowedInputs, suggester, executor, permission);
    }

    public ShortCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedShortInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        this(name, children, allowedInputs, null, executor, permission);
    }

    public ShortCommandNode(@NotNull String name, @NotNull AllowedShortInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        this(name, List.of(), allowedInputs, null, executor, permission);
    }

    public ShortCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedShortInputs allowedInputs, @Nullable String permission) {
        this(name, children, allowedInputs, null, null, permission);
    }

    public ShortCommandNode(@NotNull String name, @NotNull AllowedShortInputs allowedInputs, @Nullable String permission) {
        this(name, List.of(), allowedInputs, null, null, permission);
    }

    public ShortCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedShortInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor) {
        this(name, children, allowedInputs, null, executor, null);
    }

    public ShortCommandNode(@NotNull String name, @NotNull AllowedShortInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor) {
        this(name, List.of(), allowedInputs, null, executor, null);
    }

    public ShortCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedShortInputs allowedInputs) {
        this(name, children, allowedInputs, null, null, null);
    }

    public ShortCommandNode(@NotNull String name, @NotNull AllowedShortInputs allowedInputs) {
        this(name, List.of(), allowedInputs, null, null, null);
    }

    @Override
    public @NotNull AllowedShortInputs getAllowedInputs() {
        return (AllowedShortInputs) super.getAllowedInputs();
    }

    @Override
    public @Nullable ShortSuggester getSuggester() {
        return (ShortSuggester) super.getSuggester();
    }

    @Override
    public @NotNull Short parse(@NotNull String input) throws InputParsingException {
        try {
            return Short.parseShort(input);
        } catch (NumberFormatException e) {
            throw new InputParsingException("Could not parse as short: " + input, e);
        }
    }

    @Override
    public @NotNull ShortCommandNode withChildren(@NotNull List<CommandNode<?>> children) {
        return new ShortCommandNode(getName(), children, getAllowedInputs(), getSuggester(), getExecutor(), getPermission());
    }

    @Override
    public @NotNull ShortCommandNode withExecutor(@Nullable CommandNodeExecutor<?> executor) {
        return new ShortCommandNode(getName(), getChildren(), getAllowedInputs(), getSuggester(), executor, getPermission());
    }

    @Override
    public @NotNull ShortCommandNode withPermission(@Nullable String permission) {
        return new ShortCommandNode(getName(), getChildren(), getAllowedInputs(), getSuggester(), getExecutor(), permission);
    }
}
