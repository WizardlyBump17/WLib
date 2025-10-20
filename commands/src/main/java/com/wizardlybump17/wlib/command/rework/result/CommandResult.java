package com.wizardlybump17.wlib.command.rework.result;

import com.wizardlybump17.wlib.command.rework.node.CommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CommandResult<T> {

    boolean success();

    @Nullable T data();

    static <T> @NotNull SuccessResult<T> successful(@Nullable T data) {
        return new SuccessResult<>(data);
    }

    static <T> @NotNull ExceptionResult<T> exceptionally(@NotNull Throwable throwable) {
        return new ExceptionResult<>(throwable);
    }

    static <T> @NotNull GenericErrorResult<T> error(@NotNull String message) {
        return new GenericErrorResult<>(message);
    }

    static <T> @NotNull GenericErrorResult<T> error() {
        return new GenericErrorResult<>();
    }

    static <T> @NotNull InvalidArgumentResult<T> invalidArgument(@NotNull CommandNode<T> node, @NotNull CommandResult<T> previousResult) {
        return new InvalidArgumentResult<>(node, previousResult);
    }

    static <T> @NotNull InvalidArgumentResult<T> invalidArgument(@NotNull CommandNode<T> node, @NotNull T data) {
        return new InvalidArgumentResult<>(node, data);
    }

    static <T> @NotNull InvalidArgumentResult<T> invalidArgument(@NotNull CommandNode<T> node) {
        return new InvalidArgumentResult<>(node);
    }
}
