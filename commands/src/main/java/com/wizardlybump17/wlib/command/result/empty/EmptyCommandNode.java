package com.wizardlybump17.wlib.command.result.empty;

import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.node.CommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

final class EmptyCommandNode extends CommandNode<Object> {

    public static final @NotNull EmptyCommandNode INSTANCE = new EmptyCommandNode();

    private EmptyCommandNode() {
        super("EmptyNode", $ -> true);
    }

    @Override
    public @Nullable Object parse(@NotNull String input) {
        return null;
    }

    @Override
    public @NotNull EmptyCommandNode withChildren(@NotNull List<CommandNode<?>> children) {
        return this;
    }

    @Override
    public @NotNull EmptyCommandNode withExecutor(@Nullable CommandNodeExecutor<?> executor) {
        return this;
    }

    @Override
    public @NotNull EmptyCommandNode withPermission(@Nullable String permission) {
        return this;
    }

    @Override
    public String toString() {
        return "EmptyCommandNode{}";
    }
}
