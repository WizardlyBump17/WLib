package com.wizardlybump17.wlib.command.rework.result;

import com.wizardlybump17.wlib.command.rework.Command;
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

    static <T> @NotNull SuccessResult<T> successful(int lastInputIndex, @NotNull CommandNode<?> lastNode, @Nullable T data) {
        return new SuccessResult<>(lastInputIndex, lastNode, data);
    }

    static <T> @NotNull ExceptionResult<T> exceptionally( int lastInputIndex, @NotNull CommandNode<?> lastNode, @NotNull Throwable throwable) {
        return new ExceptionResult<>(lastInputIndex, lastNode, throwable);
    }

    static <T> @NotNull OutOfRangeInputResult<T> outOfRangeInput(int lastInputIndex, @NotNull CommandNode<?> lastNode) {
        return new OutOfRangeInputResult<>(lastInputIndex, lastNode);
    }

    static <T> @NotNull ExtraArgumentsResult<T> extraArguments(int lastInputIndex, @NotNull CommandNode<?> lastNode) {
        return new ExtraArgumentsResult<>(lastInputIndex, lastNode);
    }

    static <T> @NotNull InsufficientArgumentsResult<T> insufficientArguments(@NotNull Command command) {
        return new InsufficientArgumentsResult<>(command);
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

    static <T> @NotNull NoPermissionResult<T> noPermission(int lastInputIndex, @NotNull CommandNode<?> lastNode) {
        return new NoPermissionResult<>(lastInputIndex, lastNode);
    }

    static <T> @NotNull CommandNotFoundResult<T> commandNotFound(@NotNull String input) {
        return new CommandNotFoundResult<>(input);
    }

    //with context

    static <T> @NotNull SuccessResult<T> successful(@NotNull CommandContext context, @Nullable T data) {
        return successful(context.lastInputIndex(), context.lastNode(), data);
    }

    static <T> @NotNull ExceptionResult<T> exceptionally(@NotNull CommandContext context, @NotNull Throwable exception) {
        return exceptionally(context.lastInputIndex(), context.lastNode(), exception);
    }

    static <T> @NotNull GenericErrorResult<T> genericError(@NotNull CommandContext context, @NotNull String message) {
        return genericError(context.lastInputIndex(), context.lastNode(), message);
    }

    static <T> @NotNull GenericErrorResult<T> genericError(@NotNull CommandContext context) {
        return genericError(context.lastInputIndex(), context.lastNode());
    }

    static <T> @NotNull NoPermissionResult<T> noPermission(@NotNull CommandContext context) {
        return noPermission(context.lastInputIndex(), context.lastNode());
    }
}
