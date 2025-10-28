package com.wizardlybump17.wlib.command.rework.result;

import com.wizardlybump17.wlib.command.rework.context.CommandContext;
import com.wizardlybump17.wlib.command.rework.exception.InputParsingException;
import com.wizardlybump17.wlib.command.rework.node.CommandNode;
import com.wizardlybump17.wlib.command.rework.result.error.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CommandResult<T> {

    boolean success();

    @Nullable T data();

    int lastInputIndex();

    @NotNull CommandNode<?> lastNode();

    //without context

    static <T> @NotNull SuccessResult<T> successful(@Nullable T data, int lastInputIndex, @NotNull CommandNode<?> lastNode) {
        return new SuccessResult<>(lastInputIndex, lastNode, data);
    }

    static <T> @NotNull ExceptionResult<T> exceptionally(@NotNull Throwable throwable, int lastInputIndex, @NotNull CommandNode<?> lastNode) {
        return new ExceptionResult<>(lastInputIndex, lastNode, throwable);
    }

    static <T> @NotNull OutOfRangeInputResult<T> outOfRangeInput(int lastInputIndex, @NotNull CommandNode<?> lastNode) {
        return new OutOfRangeInputResult<>(lastInputIndex, lastNode);
    }

    static <T> @NotNull ExtraArgumentsResult<T> extraArguments(int lastInputIndex, @NotNull CommandNode<?> lastNode) {
        return new ExtraArgumentsResult<>(lastInputIndex, lastNode);
    }

    static <T> @NotNull InsufficientArgumentsResult<T> insufficientArguments(int lastInputIndex, @NotNull CommandNode<?> lastNode) {
        return new InsufficientArgumentsResult<>(lastInputIndex, lastNode);
    }

    static <T> @NotNull ParseInputExceptionResult<T> parseInputException(int lastInputIndex, @NotNull CommandNode<?> lastNode, @NotNull InputParsingException exception) {
        return new ParseInputExceptionResult<>(lastInputIndex, lastNode, exception);
    }

    static <T> @NotNull CommandNodeExecutorNotFoundResult<T> noCommandNodeExecutor(int lastInputIndex, @NotNull CommandNode<?> lastNode) {
        return new CommandNodeExecutorNotFoundResult<>(lastInputIndex, lastNode);
    }

    static <T> @NotNull GenericErrorResult<T> genericError(int lastInputIndex, @NotNull CommandNode<?> lastNode, @NotNull String message) {
        return new GenericErrorResult<>(lastInputIndex, lastNode, message);
    }

    static <T> @NotNull GenericErrorResult<T> genericError(int lastInputIndex, @NotNull CommandNode<?> lastNode) {
        return new GenericErrorResult<>(lastInputIndex, lastNode);
    }

    //with context

    static <T> @NotNull SuccessResult<T> successful(@NotNull CommandContext context, @Nullable T data) {
        return successful(data, context.lastInputIndex(), context.lastNode());
    }

    static <T> @NotNull ExceptionResult<T> exceptionally(@NotNull CommandContext context, @NotNull Throwable exception) {
        return exceptionally(exception, context.lastInputIndex(), context.lastNode());
    }

    static <T> @NotNull GenericErrorResult<T> genericError(@NotNull CommandContext context, @NotNull String message) {
        return genericError(context.lastInputIndex(), context.lastNode(), message);
    }

    static <T> @NotNull GenericErrorResult<T> genericError(@NotNull CommandContext context) {
        return genericError(context.lastInputIndex(), context.lastNode());
    }
}
