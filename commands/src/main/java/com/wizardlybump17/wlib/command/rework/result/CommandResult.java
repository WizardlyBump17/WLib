package com.wizardlybump17.wlib.command.rework.result;

import com.wizardlybump17.wlib.command.rework.node.CommandNode;
import com.wizardlybump17.wlib.command.rework.result.error.ExceptionResult;
import com.wizardlybump17.wlib.command.rework.result.error.ExtraArgumentsResult;
import com.wizardlybump17.wlib.command.rework.result.error.InsufficientArgumentsResult;
import com.wizardlybump17.wlib.command.rework.result.error.InvalidArgumentResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CommandResult<T> {

    boolean success();

    @Nullable T data();

    int lastInputIndex();

    @NotNull CommandNode<?> lastNode();

    static <T> @NotNull SuccessResult<T> successful(@Nullable T data, int lastInputIndex, @NotNull CommandNode<?> lastNode) {
        return new SuccessResult<>(lastInputIndex, lastNode, data);
    }

    static <T> @NotNull ExceptionResult<T> exceptionally(@NotNull Throwable throwable, int lastInputIndex, @NotNull CommandNode<?> lastNode) {
        return new ExceptionResult<>(lastInputIndex, lastNode, throwable);
    }

    static <T> @NotNull InvalidArgumentResult<T> invalidArgument(int lastInputIndex, @NotNull CommandNode<?> lastNode) {
        return new InvalidArgumentResult<>(lastInputIndex, lastNode);
    }

    static <T> @NotNull ExtraArgumentsResult<T> extraArguments(int lastInputIndex, @NotNull CommandNode<?> lastNode) {
        return new ExtraArgumentsResult<>(lastInputIndex, lastNode);
    }

    static <T> @NotNull InsufficientArgumentsResult<T> insufficientArguments(int lastInputIndex, @NotNull CommandNode<?> lastNode) {
        return new InsufficientArgumentsResult<>(lastInputIndex, lastNode);
    }
}
