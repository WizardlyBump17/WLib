package com.wizardlybump17.wlib.command.node.primitive.number;

import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.AllowedInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.suggestion.Suggester;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class NumberCommandNode<N extends Number> extends CommandNode<N> {

    public NumberCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedInputs<N> allowedInputs, @Nullable Suggester<N> suggester, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, allowedInputs, suggester, executor, permission);
    }

    @Override
    public abstract @NotNull NumberCommandNode<N> withChildren(@NotNull List<CommandNode<?>> children);

    @Override
    public abstract @NotNull NumberCommandNode<N> withExecutor(@Nullable CommandNodeExecutor<?> executor);

    @Override
    public abstract @NotNull NumberCommandNode<N> withPermission(@Nullable String permission);
}
