package com.wizardlybump17.wlib.command.node.primitive;

import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.primitive.PrimitiveAllowedInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class PrimitiveCommandNode<P> extends CommandNode<P> {

    public PrimitiveCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull PrimitiveAllowedInputs<P> allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, allowedInputs, executor, permission);
    }

    public PrimitiveCommandNode(@NotNull String name, @NotNull PrimitiveAllowedInputs<P> allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, allowedInputs, executor, permission);
    }

    public PrimitiveCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull PrimitiveAllowedInputs<P> allowedInputs, @Nullable String permission) {
        super(name, children, allowedInputs, permission);
    }

    public PrimitiveCommandNode(@NotNull String name, @NotNull PrimitiveAllowedInputs<P> allowedInputs, @Nullable String permission) {
        super(name, allowedInputs, permission);
    }

    public PrimitiveCommandNode(@NotNull String name, @NotNull PrimitiveAllowedInputs<P> allowedInputs, @Nullable CommandNodeExecutor<?> executor) {
        super(name, allowedInputs, executor);
    }

    public PrimitiveCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull PrimitiveAllowedInputs<P> allowedInputs) {
        super(name, children, allowedInputs);
    }

    public PrimitiveCommandNode(@NotNull String name, @NotNull PrimitiveAllowedInputs<P> allowedInputs) {
        super(name, allowedInputs);
    }
}
