package com.wizardlybump17.wlib.command.rework.result.error;

import com.wizardlybump17.wlib.command.rework.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.rework.node.CommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record CommandNotFoundResult<T>(@NotNull String input) implements UnsuccessResult<T> {

    @Override
    public int lastInputIndex() {
        return -1;
    }

    @Override
    public @NotNull CommandNode<?> lastNode() {
        return DummyNode.INSTANCE;
    }

    private static final class DummyNode extends CommandNode<Object> {

        public static final @NotNull DummyNode INSTANCE = new DummyNode();

        private DummyNode() {
            super("DummyNode", $ -> true);
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
        public @NotNull DummyNode withExecutor(@NotNull CommandNodeExecutor<?> executor) {
            return this;
        }
    }
}
