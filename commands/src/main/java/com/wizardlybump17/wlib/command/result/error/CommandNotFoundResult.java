package com.wizardlybump17.wlib.command.result.error;

import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.node.CommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record CommandNotFoundResult<T>(@NotNull String input, @NotNull ErrorDetails errorDetails) implements UnsuccessResult<T> {

    public static final @NotNull String ID = "WLib:Unsuccess/CommandNotFound";

    @Override
    public int lastInputIndex() {
        return 0;
    }

    @Override
    public @NotNull CommandNode<?> lastNode() {
        return DummyNode.INSTANCE;
    }

    @Override
    public @NotNull String id() {
        return ID;
    }

    private static final class DummyNode extends CommandNode<Object> {

        public static final @NotNull DummyNode INSTANCE = new DummyNode();

        private DummyNode() {
            super("DummyNode", List.of(), $ -> true, null, null, null);
        }

        @Override
        public @Nullable Object parse(@NotNull String input) {
            return null;
        }

        @Override
        public @NotNull DummyNode withChildren(@NotNull List<CommandNode<?>> children) {
            return this;
        }

        @Override
        public @NotNull DummyNode withExecutor(@Nullable CommandNodeExecutor<?> executor) {
            return this;
        }

        @Override
        public @NotNull CommandNode<Object> withPermission(@Nullable String permission) {
            return this;
        }
    }
}
