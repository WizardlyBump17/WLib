package com.wizardlybump17.wlib.command.node.primitive;

import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.AllowedInputs;
import com.wizardlybump17.wlib.command.input.primitive.PrimitiveAllowedInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.suggestion.Suggester;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public abstract class AbstractPrimitiveCommandNode<P> extends CommandNode<P> implements PrimitiveCommandNode {

    public AbstractPrimitiveCommandNode(@NotNull String name, @NotNull @Unmodifiable List<CommandNode<?>> children, @NotNull AllowedInputs<P> allowedInputs, @Nullable Suggester<P> suggester, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, allowedInputs, suggester, executor, permission);
    }

    public AbstractPrimitiveCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull PrimitiveAllowedInputs<P> allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, allowedInputs, executor, permission);
    }

    public AbstractPrimitiveCommandNode(@NotNull String name, @NotNull PrimitiveAllowedInputs<P> allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, allowedInputs, executor, permission);
    }

    public AbstractPrimitiveCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull PrimitiveAllowedInputs<P> allowedInputs, @Nullable String permission) {
        super(name, children, allowedInputs, permission);
    }

    public AbstractPrimitiveCommandNode(@NotNull String name, @NotNull PrimitiveAllowedInputs<P> allowedInputs, @Nullable String permission) {
        super(name, allowedInputs, permission);
    }

    public AbstractPrimitiveCommandNode(@NotNull String name, @NotNull PrimitiveAllowedInputs<P> allowedInputs, @Nullable CommandNodeExecutor<?> executor) {
        super(name, allowedInputs, executor);
    }

    public AbstractPrimitiveCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull PrimitiveAllowedInputs<P> allowedInputs) {
        super(name, children, allowedInputs);
    }

    public AbstractPrimitiveCommandNode(@NotNull String name, @NotNull PrimitiveAllowedInputs<P> allowedInputs) {
        super(name, allowedInputs);
    }
}
