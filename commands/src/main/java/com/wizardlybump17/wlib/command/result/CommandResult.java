package com.wizardlybump17.wlib.command.result;

import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.exception.InvalidInputException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class CommandResult<T> {

    private final boolean success;
    private final @Nullable T data;
    private final @NotNull Type type;
    private final @Nullable ErrorDetails errorDetails;

    private CommandResult(boolean success, @Nullable T data, @NotNull Type type, @Nullable ErrorDetails errorDetails) {
        this.success = success;
        this.data = data;
        this.type = type;
        this.errorDetails = errorDetails;
    }

    public boolean success() {
        return success;
    }

    public @Nullable T data() {
        return data;
    }

    public @NotNull Type type() {
        return type;
    }

    public @Nullable ErrorDetails errorDetails() {
        return errorDetails;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (other == null || getClass() != other.getClass())
            return false;
        CommandResult<?> that = (CommandResult<?>) other;
        return success == that.success && Objects.equals(data, that.data) && Objects.equals(type, that.type) && Objects.equals(errorDetails, that.errorDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, data, type, errorDetails);
    }

    @Override
    public String toString() {
        return "CommandResult{" +
                "success=" + success +
                ", data=" + data +
                ", type='" + type + '\'' +
                ", errorDetails=" + errorDetails +
                '}';
    }

    //SUCCESS

    public static <T> @NotNull CommandResult<T> successful() {
        return new CommandResult<>(true, null, Type.SUCCESS, null);
    }

    public static <T> @NotNull CommandResult<T> successful(@Nullable T data) {
        return new CommandResult<>(true, data, Type.SUCCESS, null);
    }

    public static <T> @NotNull CommandResult<T> noContent() {
        return new CommandResult<>(true, null, Type.NO_CONTENT, null);
    }

    public static <T> @NotNull CommandResult<T> noContent(@Nullable T data) {
        return new CommandResult<>(true, data, Type.NO_CONTENT, null);
    }

    //ERROR

    public static <T> @NotNull CommandResult<T> badRequest() {
        return new CommandResult<>(false, null, Type.BAD_REQUEST, null);
    }

    public static <T> @NotNull CommandResult<T> badRequest(@NotNull ErrorDetails errorDetails) {
        return new CommandResult<>(false, null, Type.BAD_REQUEST, errorDetails);
    }

    public static <T> @NotNull CommandResult<T> conflict() {
        return new CommandResult<>(false, null, Type.CONFLICT, null);
    }

    public static <T> @NotNull CommandResult<T> conflict(@NotNull ErrorDetails errorDetails) {
        return new CommandResult<>(false, null, Type.CONFLICT, errorDetails);
    }

    public static <T> @NotNull CommandResult<T> forbidden() {
        return new CommandResult<>(false, null, Type.FORBIDDEN, null);
    }

    public static <T> @NotNull CommandResult<T> forbidden(@NotNull ErrorDetails errorDetails) {
        return new CommandResult<>(false, null, Type.FORBIDDEN, errorDetails);
    }

    public static <T> @NotNull CommandResult<T> genericError() {
        return new CommandResult<>(false, null, Type.GENERIC_ERROR, null);
    }

    public static <T> @NotNull CommandResult<T> genericError(@NotNull ErrorDetails errorDetails) {
        return new CommandResult<>(false, null, Type.GENERIC_ERROR, errorDetails);
    }

    public static <T> @NotNull CommandResult<T> invalidSender() {
        return new CommandResult<>(false, null, Type.INVALID_SENDER, null);
    }

    public static <T> @NotNull CommandResult<T> invalidSender(@NotNull ErrorDetails errorDetails) {
        return new CommandResult<>(false, null, Type.INVALID_SENDER, errorDetails);
    }

    public static <T> @NotNull CommandResult<T> notFound() {
        return new CommandResult<>(false, null, Type.NOT_FOUND, null);
    }

    public static <T> @NotNull CommandResult<T> notFound(@NotNull ErrorDetails errorDetails) {
        return new CommandResult<>(false, null, Type.NOT_FOUND, errorDetails);
    }

    public static <T> @NotNull CommandResult<T> unauthorized() {
        return new CommandResult<>(false, null, Type.UNAUTHORIZED, null);
    }

    public static <T> @NotNull CommandResult<T> unauthorized(@NotNull ErrorDetails errorDetails) {
        return new CommandResult<>(false, null, Type.UNAUTHORIZED, errorDetails);
    }

    public static <T> @NotNull CommandResult<T> unprocessableContent() {
        return new CommandResult<>(false, null, Type.UNPROCESSABLE_CONTENT, null);
    }

    public static <T> @NotNull CommandResult<T> unprocessableContent(@NotNull ErrorDetails errorDetails) {
        return new CommandResult<>(false, null, Type.UNPROCESSABLE_CONTENT, errorDetails);
    }

    public static <T> @NotNull CommandResult<T> notImplemented() {
        return new CommandResult<>(false, null, Type.NOT_IMPLEMENTED, null);
    }

    public static <T> @NotNull CommandResult<T> notImplemented(@NotNull ErrorDetails errorDetails) {
        return new CommandResult<>(false, null, Type.NOT_IMPLEMENTED, errorDetails);
    }

    public enum Type {

        SUCCESS,
        NO_CONTENT,
        BAD_REQUEST,
        CONFLICT,
        FORBIDDEN,
        GENERIC_ERROR,
        INVALID_SENDER,
        NOT_FOUND,
        UNAUTHORIZED,
        UNPROCESSABLE_CONTENT,
        NOT_IMPLEMENTED;

        public boolean isSuccess() {
            return switch (this) {
                case SUCCESS, NO_CONTENT -> true;
                default -> false;
            };
        }
    }

    public record ErrorDetails(@NotNull String code, @NotNull String message, @NotNull String detail) {

        private static final @NotNull ErrorDetails EMPTY_INPUT = new ErrorDetails(CommandErrorCodes.BAD_REQUEST_EMPTY_INPUT, "The input can not be empty", "The input can not be empty");

        public static @NotNull ErrorDetails noCommandExecutor(@NotNull String command, @NotNull String node) {
            return new ErrorDetails(CommandErrorCodes.NOT_IMPLEMENTED_NO_COMMAND_EXECUTOR, "No executor found for this command", "The command \"" + command + "\" does not have an executor at the node \"" + node + "\"");
        }

        public static @NotNull ErrorDetails parseError(@NotNull String command, @NotNull String node, @NotNull InputParsingException exception) {
            return new ErrorDetails(CommandErrorCodes.BAD_REQUEST_PARSE_ERROR, "Error while parsing the input", "Error while parsing the input at the command \"" + command + "\", node \"" + node + "\": " + exception.getMessage());
        }

        public static @NotNull ErrorDetails inputError(@NotNull String command, @NotNull String node, @Nullable String input, @NotNull InvalidInputException exception) {
            return new ErrorDetails(CommandErrorCodes.UNPROCESSABLE_CONTENT_INVALID_INPUT, "Input not accepted", "The input \"" + input + "\" is not accepted by \"" + node + "\" at \"" + command + "\": " + exception.getMessage());
        }

        public static @NotNull ErrorDetails noPermission(@NotNull String sender, @NotNull String permission) {
            return new ErrorDetails(CommandErrorCodes.FORBIDDEN_NO_PERMISSION, "Not enough permissions", sender + " does not have the permission \"" + permission + "\"");
        }

        public static @NotNull ErrorDetails emptyInput() {
            return EMPTY_INPUT;
        }

        public static @NotNull ErrorDetails nodeNotFound(int index, @Nullable String node) {
            return new ErrorDetails(CommandErrorCodes.NOT_FOUND_NODE_NOT_FOUND, "Node not found", "Could not find a node for \"" + node + "\" at index " + index);
        }
    }
}
