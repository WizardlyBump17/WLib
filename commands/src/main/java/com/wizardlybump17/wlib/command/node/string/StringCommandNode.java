package com.wizardlybump17.wlib.command.node.string;

import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.AllowedInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StringCommandNode extends CommandNode<String> {

    public StringCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedInputs<String> allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, allowedInputs, executor, permission);
    }

    public StringCommandNode(@NotNull String name, @NotNull AllowedInputs<String> allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, allowedInputs, executor, permission);
    }

    public StringCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedInputs<String> allowedInputs, @Nullable String permission) {
        super(name, children, allowedInputs, permission);
    }

    public StringCommandNode(@NotNull String name, @NotNull AllowedInputs<String> allowedInputs, @Nullable String permission) {
        super(name, allowedInputs, permission);
    }

    public StringCommandNode(@NotNull String name, @NotNull AllowedInputs<String> allowedInputs, @Nullable CommandNodeExecutor<?> executor) {
        super(name, allowedInputs, executor);
    }

    public StringCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedInputs<String> allowedInputs) {
        super(name, children, allowedInputs);
    }

    public StringCommandNode(@NotNull String name, @NotNull AllowedInputs<String> allowedInputs) {
        super(name, allowedInputs);
    }

    @Override
    public @Nullable String parse(@NotNull String input) throws InputParsingException {
        return input;
    }

    @Override
    public @NotNull StringCommandNode withChildren(@NotNull List<CommandNode<?>> children) {
        return new StringCommandNode(getName(), children, getAllowedInputs(), getExecutor(), getPermission());
    }

    @Override
    public @NotNull StringCommandNode withExecutor(@Nullable CommandNodeExecutor<?> executor) {
        return new StringCommandNode(getName(), getChildren(), getAllowedInputs(), executor, getPermission());
    }

    @Override
    public @NotNull StringCommandNode withPermission(@Nullable String permission) {
        return new StringCommandNode(getName(), getChildren(), getAllowedInputs(), getExecutor(), permission);
    }
}
