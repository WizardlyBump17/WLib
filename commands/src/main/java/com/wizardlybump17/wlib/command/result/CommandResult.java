package com.wizardlybump17.wlib.command.result;

import com.wizardlybump17.wlib.command.context.CommandContext;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.result.error.*;
import com.wizardlybump17.wlib.command.result.success.NoContentResult;
import com.wizardlybump17.wlib.command.result.success.SuccessResult;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CommandResult<T> {

    boolean success();

    @Nullable T data();

    @NotNull String id();

    @Nullable ErrorDetails errorDetails();

    //without context

    static <T> @NotNull SuccessResult<T> successful(int lastInputIndex, @NotNull CommandNode<?> lastNode, @Nullable T data) {
        return new SuccessResult<>(lastInputIndex, lastNode, data);
    }

    static <T> @NotNull ExceptionResult<T> exceptionally(@NotNull ErrorDetails errorDetails) {
        return new ExceptionResult<>(errorDetails);
    }

    static <T> @NotNull OutOfRangeInputResult<T> outOfRangeInput(@NotNull ErrorDetails errorDetails) {
        return new OutOfRangeInputResult<>(errorDetails);
    }

    static <T> @NotNull ExtraArgumentsResult<T> extraArguments(@NotNull ErrorDetails errorDetails) {
        return new ExtraArgumentsResult<>(errorDetails);
    }

    static <T> @NotNull InsufficientArgumentsResult<T> insufficientArguments(@NotNull ErrorDetails errorDetails) {
        return new InsufficientArgumentsResult<>(errorDetails);
    }

    static <T> @NotNull ParseInputExceptionResult<T> parseInputException(@NotNull ErrorDetails errorDetails) {
        return new ParseInputExceptionResult<>(errorDetails);
    }

    static <T> @NotNull CommandNodeExecutorNotFoundResult<T> noCommandNodeExecutor(@NotNull ErrorDetails errorDetails) {
        return new CommandNodeExecutorNotFoundResult<>(errorDetails);
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

    static <T> @NotNull InvalidSenderResult<T> invalidSender(int lastInputIndex, @NotNull CommandNode<?> lastNode, @NotNull CommandSender<?> sender, @NotNull Class<? extends CommandSender<?>> expectedSender) {
        return new InvalidSenderResult<>(lastInputIndex, lastNode, sender, expectedSender);
    }

    static <T> @NotNull NoContentResult<T> noContent(int lastInputIndex, @NotNull CommandNode<?> lastNode) {
        return new NoContentResult<>(lastInputIndex, lastNode);
    }

    static <T> @NotNull NotFoundResult<T> notFound(int lastInputIndex, @NotNull CommandNode<?> lastNode) {
        return new NotFoundResult<>(lastInputIndex, lastNode);
    }

    static <T> @NotNull ConflictResult<T> conflict(int lastInputIndex, @NotNull CommandNode<?> lastNode) {
        return new ConflictResult<>(lastInputIndex, lastNode);
    }

    static <T> @NotNull BadRequestResult<T> badRequest(int lastInputIndex, @NotNull CommandNode<?> lastNode) {
        return new BadRequestResult<>(lastInputIndex, lastNode);
    }

    static <T> @NotNull UnprocessableContentResult<T> unprocessableContent(int lastInputIndex, @NotNull CommandNode<?> lastNode) {
        return new UnprocessableContentResult<>(lastInputIndex, lastNode);
    }

    static <T> @NotNull UnauthorizedResult<T> unauthorized(int lastInputIndex, @NotNull CommandNode<?> lastNode, @Nullable String message) {
        return new UnauthorizedResult<>(lastInputIndex, lastNode, message);
    }

    static <T> @NotNull ForbiddenResult<T> forbidden(int lastInputIndex, @NotNull CommandNode<?> lastNode, @Nullable String message) {
        return new ForbiddenResult<>(lastInputIndex, lastNode, message);
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

    static <T> @NotNull NoPermissionResult<T> noPermission(@NotNull ErrorDetails errorDetails) {
        return new NoPermissionResult<>(errorDetails);
    }

    static <T> @NotNull InvalidSenderResult<T> invalidSender(@NotNull CommandContext context, @NotNull Class<? extends CommandSender<?>> expectedSender) {
        return invalidSender(context.lastInputIndex(), context.lastNode(), context.sender(), expectedSender);
    }

    static <T> @NotNull NoContentResult<T> noContent(@NotNull CommandContext context) {
        return noContent(context.lastInputIndex(), context.lastNode());
    }

    static <T> @NotNull NotFoundResult<T> notFound(@NotNull CommandContext context) {
        return notFound(context.lastInputIndex(), context.lastNode());
    }

    static <T> @NotNull ConflictResult<T> conflict(@NotNull CommandContext context) {
        return conflict(context.lastInputIndex(), context.lastNode());
    }

    static <T> @NotNull BadRequestResult<T> badRequest(@NotNull CommandContext context) {
        return badRequest(context.lastInputIndex(), context.lastNode());
    }

    static <T> @NotNull UnprocessableContentResult<T> unprocessableContent(@NotNull CommandContext context) {
        return unprocessableContent(context.lastInputIndex(), context.lastNode());
    }

    static <T> @NotNull UnauthorizedResult<T> unauthorized(@NotNull CommandContext context, @Nullable String message) {
        return unauthorized(context.lastInputIndex(), context.lastNode(), message);
    }

    static <T> @NotNull ForbiddenResult<T> forbidden(@NotNull CommandContext context, @NotNull ErrorDetails errorDetails) {
        return forbidden(context.lastInputIndex(), context.lastNode(), message);
    }
}
