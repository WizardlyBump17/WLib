package com.wizardlybump17.wlib.command.node;

import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.string.AllowedStringInputs;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import com.wizardlybump17.wlib.command.suggestion.string.StringSuggester;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LiteralCommandNode extends CommandNode<String> {

    public LiteralCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, AllowedStringInputs.valueIgnoreCase(name), StringSuggester.value(name), executor, permission);
    }

    public LiteralCommandNode(@NotNull String name, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        this(name, List.of(), executor, permission);
    }

    public LiteralCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @Nullable String permission) {
        this(name, children, null, permission);
    }

    public LiteralCommandNode(@NotNull String name, @Nullable String permission) {
        this(name, List.of(), null, permission);
    }

    public LiteralCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @Nullable CommandNodeExecutor<?> executor) {
        super(name, children, AllowedStringInputs.valueIgnoreCase(name), executor, null);
    }

    public LiteralCommandNode(@NotNull String name, @Nullable CommandNodeExecutor<?> executor) {
        this(name, List.of(), executor, null);
    }

    public LiteralCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children) {
        this(name, children, null, null);
    }

    public LiteralCommandNode(@NotNull String name) {
        this(name, List.of(), null, null);
    }

    @Override
    public @NotNull AllowedStringInputs.Value getAllowedInputs() {
        return (AllowedStringInputs.Value) super.getAllowedInputs();
    }

    @Override
    public @NotNull String parse(@NotNull String input) {
        return input;
    }

    @Override
    public @NotNull List<String> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> args, @NotNull String currentInput) {
        return List.of(getName());
    }

    @Override
    public @NotNull LiteralCommandNode withChildren(@NotNull List<CommandNode<?>> children) {
        return new LiteralCommandNode(getName(), children, getExecutor(), getPermission());
    }

    @Override
    public @NotNull CommandNode<String> withExecutor(@Nullable CommandNodeExecutor<?> executor) {
        return new LiteralCommandNode(getName(), getChildren(), executor, getPermission());
    }

    @Override
    public @NotNull CommandNode<String> withPermission(@Nullable String permission) {
        return new LiteralCommandNode(getName(), getChildren(), getExecutor(), permission);
    }
}
