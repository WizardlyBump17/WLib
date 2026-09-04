package com.wizardlybump17.wlib.command.result;

import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.exception.InvalidInputException;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class CommandResult<T> {

    private final @Nullable T data;
    private final @NotNull Type type;
    private final @NotNull String resultCode;
    private final @Nullable String message;

    private static final @NotNull CommandResult<?> SUCCESSFUL = builder()
            .type(Type.SUCCESS)
            .resultCode(CommandErrorCodes.SUCCESS)
            .build();
    private static final @NotNull CommandResult<?> NO_CONTENT_RESULT = builder()
            .type(Type.NO_CONTENT)
            .resultCode(CommandErrorCodes.NO_CONTENT)
            .build();

    private static final @NotNull CommandResult<?> BAD_REQUEST_RESULT = builder()
            .type(Type.BAD_REQUEST)
            .resultCode(CommandErrorCodes.BAD_REQUEST_GENERIC)
            .message("Bad request")
            .build();
    private static final @NotNull CommandResult<?> CONFLICT_RESULT = builder()
            .type(Type.CONFLICT)
            .resultCode(CommandErrorCodes.CONFLICT_GENERIC)
            .message("Conflict")
            .build();
    private static final @NotNull CommandResult<?> FORBIDDEN_RESULT = builder()
            .type(Type.FORBIDDEN)
            .resultCode(CommandErrorCodes.FORBIDDEN_GENERIC)
            .message("Forbidden")
            .build();
    private static final @NotNull CommandResult<?> GENERIC_ERROR_RESULT = builder()
            .type(Type.GENERIC_ERROR)
            .resultCode(CommandErrorCodes.GENERIC_ERROR_GENERIC)
            .message("Internal error")
            .build();
    private static final @NotNull CommandResult<?> INVALID_SENDER_RESULT = builder()
            .type(Type.INVALID_SENDER)
            .resultCode(CommandErrorCodes.INVALID_SENDER_GENERIC)
            .message("Invalid command sender")
            .build();
    private static final @NotNull CommandResult<?> NOT_FOUND_RESULT = builder()
            .type(Type.NOT_FOUND)
            .resultCode(CommandErrorCodes.NOT_FOUND_GENERIC)
            .message("Not found")
            .build();
    private static final @NotNull CommandResult<?> UNAUTHORIZED_RESULT = builder()
            .type(Type.UNAUTHORIZED)
            .resultCode(CommandErrorCodes.UNAUTHORIZED_GENERIC)
            .message("Unauthorized")
            .build();
    private static final @NotNull CommandResult<?> UNPROCESSABLE_CONTENT_RESULT = builder()
            .type(Type.UNPROCESSABLE_CONTENT)
            .resultCode(CommandErrorCodes.UNPROCESSABLE_CONTENT_GENERIC)
            .message("Unprocessable content")
            .build();
    private static final @NotNull CommandResult<?> NOT_IMPLEMENTED_RESULT = builder()
            .type(Type.NOT_IMPLEMENTED)
            .resultCode(CommandErrorCodes.NOT_IMPLEMENTED_GENERIC)
            .message("Not implemented")
            .build();

    private CommandResult(@Nullable T data, @NotNull Type type, @NotNull String resultCode, @Nullable String message) {
        this.data = data;
        this.type = type;
        this.resultCode = resultCode;
        this.message = message;
    }

    public boolean success() {
        return type.isSuccess();
    }

    public @Nullable T data() {
        return data;
    }

    public @NotNull CommandResult<T> data(@Nullable T data) {
        return new CommandResult<>(data, type, resultCode, message);
    }

    public @NotNull Type type() {
        return type;
    }

    public @NotNull CommandResult<T> type(@NotNull Type type) {
        return new CommandResult<>(data, type, resultCode, message);
    }

    public @NotNull String resultCode() {
        return resultCode;
    }

    public @NotNull CommandResult<T> resultCode(@NotNull String resultCode) {
        return new CommandResult<>(data, type, resultCode, message);
    }

    public @Nullable String message() {
        return message;
    }

    public @NotNull CommandResult<T> message(@Nullable String message) {
        return new CommandResult<>(data, type, resultCode, message);
    }

    public @NotNull Builder<T> toBuilder() {
        return new Builder<>(data, type, resultCode, message);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (other == null || getClass() != other.getClass())
            return false;
        CommandResult<?> that = (CommandResult<?>) other;
        return Objects.equals(data, that.data)
                && Objects.equals(type, that.type)
                && Objects.equals(resultCode, that.resultCode)
                && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, type, resultCode, message);
    }

    @Override
    public String toString() {
        return "CommandResult{" +
                "data=" + data +
                ", type=" + type +
                ", resultCode='" + resultCode + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    //SUCCESS

    @SuppressWarnings("unchecked")
    public static <T> @NotNull CommandResult<T> successful() {
        return (CommandResult<T>) SUCCESSFUL;
    }

    public static <T> @NotNull CommandResult<T> successful(@Nullable T data) {
        return CommandResult.<T>builder()
                .type(Type.SUCCESS)
                .resultCode(CommandErrorCodes.SUCCESS)
                .data(data)
                .build();
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull CommandResult<T> noContent() {
        return (CommandResult<T>) NO_CONTENT_RESULT;
    }

    public static <T> @NotNull CommandResult<T> noContent(@Nullable T data) {
        return CommandResult.<T>builder()
                .type(Type.NO_CONTENT)
                .resultCode(CommandErrorCodes.NO_CONTENT)
                .data(data)
                .build();
    }

    //ERROR

    @SuppressWarnings("unchecked")
    public static <T> @NotNull CommandResult<T> badRequest() {
        return (CommandResult<T>) BAD_REQUEST_RESULT;
    }

    public static <T> @NotNull CommandResult.Builder<T> badRequest(@NotNull String resultCode) {
        return CommandResult.<T>builder()
                .type(Type.BAD_REQUEST)
                .resultCode(resultCode)
                .message("Bad request");
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull CommandResult<T> conflict() {
        return (CommandResult<T>) CONFLICT_RESULT;
    }

    public static <T> @NotNull CommandResult.Builder<T> conflict(@NotNull String resultCode) {
        return CommandResult.<T>builder()
                .type(Type.CONFLICT)
                .resultCode(resultCode)
                .message("Conflict");
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull CommandResult<T> forbidden() {
        return (CommandResult<T>) FORBIDDEN_RESULT;
    }

    public static <T> @NotNull CommandResult.Builder<T> forbidden(@NotNull String resultCode) {
        return CommandResult.<T>builder()
                .type(Type.FORBIDDEN)
                .resultCode(resultCode)
                .message("Forbidden");
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull CommandResult<T> genericError() {
        return (CommandResult<T>) GENERIC_ERROR_RESULT;
    }

    public static <T> @NotNull CommandResult.Builder<T> genericError(@NotNull String resultCode) {
        return CommandResult.<T>builder()
                .type(Type.GENERIC_ERROR)
                .resultCode(resultCode)
                .message("Internal error");
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull CommandResult<T> invalidSender() {
        return (CommandResult<T>) INVALID_SENDER_RESULT;
    }

    public static <T> @NotNull CommandResult.Builder<T> invalidSender(@NotNull String resultCode) {
        return CommandResult.<T>builder()
                .type(Type.INVALID_SENDER)
                .resultCode(resultCode)
                .message("Invalid command sender");
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull CommandResult<T> notFound() {
        return (CommandResult<T>) NOT_FOUND_RESULT;
    }

    public static <T> @NotNull CommandResult.Builder<T> notFound(@NotNull String resultCode) {
        return CommandResult.<T>builder()
                .type(Type.NOT_FOUND)
                .resultCode(resultCode)
                .message("Not found");
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull CommandResult<T> unauthorized() {
        return (CommandResult<T>) UNAUTHORIZED_RESULT;
    }

    public static <T> @NotNull CommandResult.Builder<T> unauthorized(@NotNull String resultCode) {
        return CommandResult.<T>builder()
                .type(Type.UNAUTHORIZED)
                .resultCode(resultCode)
                .message("Unauthorized");
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull CommandResult<T> unprocessableContent() {
        return (CommandResult<T>) UNPROCESSABLE_CONTENT_RESULT;
    }

    public static <T> @NotNull CommandResult.Builder<T> unprocessableContent(@NotNull String resultCode) {
        return CommandResult.<T>builder()
                .type(Type.UNPROCESSABLE_CONTENT)
                .resultCode(resultCode)
                .message("Unprocessable content");
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull CommandResult<T> notImplemented() {
        return (CommandResult<T>) NOT_IMPLEMENTED_RESULT;
    }

    public static <T> @NotNull CommandResult.Builder<T> notImplemented(@NotNull String resultCode) {
        return CommandResult.<T>builder()
                .type(Type.NOT_IMPLEMENTED)
                .resultCode(resultCode)
                .message("Not implemented");
    }

    public static <T> @NotNull Builder<T> builder() {
        return new Builder<>();
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

    public static final class Builder<T> {

        private @Nullable T data;
        private @Nullable Type type;
        private @Nullable String resultCode;
        private @Nullable String message;

        private Builder(@Nullable T data, @Nullable Type type, @Nullable String resultCode, @Nullable String message) {
            this.data = data;
            this.type = type;
            this.resultCode = resultCode;
            this.message = message;
        }

        private Builder() {
        }

        public @Nullable T data() {
            return data;
        }

        public @NotNull Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public @Nullable Type type() {
            return type;
        }

        public @NotNull Builder<T> type(@Nullable Type type) {
            this.type = type;
            return this;
        }

        public @Nullable String resultCode() {
            return resultCode;
        }

        public @NotNull Builder<T> resultCode(@Nullable String resultCode) {
            this.resultCode = resultCode;
            return this;
        }

        public @Nullable String message() {
            return message;
        }

        public @NotNull Builder<T> message(@Nullable String message) {
            this.message = message;
            return this;
        }

        public @NotNull CommandResult<T> build() {
            return new CommandResult<>(
                    data,
                    Objects.requireNonNull(type, "The type can not be null"),
                    Objects.requireNonNull(resultCode, "The resultCode can not be null"),
                    message
            );
        }
    }

    public static final class Errors {

        private static final @NotNull CommandResult<String> EMPTY_INPUT = CommandResult.<String>builder()
                .type(Type.BAD_REQUEST)
                .resultCode(CommandErrorCodes.BAD_REQUEST_EMPTY_INPUT)
                .message("The input can not be empty")
                .build();

        private Errors() {
        }

        @ApiStatus.Internal
        public static @NotNull CommandResult<String> emptyInput() {
            return EMPTY_INPUT;
        }

        @ApiStatus.Internal
        public static @NotNull CommandResult<String> commandNotFound(@NotNull String command) {
            return CommandResult.<String>builder()
                    .type(CommandResult.Type.NOT_FOUND)
                    .resultCode(CommandErrorCodes.NOT_FOUND_NODE_NOT_FOUND)
                    .message("Command \"" + command + "\" not found")
                    .build();
        }

        @ApiStatus.Internal
        public static @NotNull CommandResult<String> parseError(@NotNull String input, @NotNull String node, int position, @NotNull InputParsingException exception) {
            return CommandResult.<String>builder()
                    .type(CommandResult.Type.BAD_REQUEST)
                    .resultCode(CommandErrorCodes.BAD_REQUEST_PARSE_ERROR)
                    .message("Error while parsing the input \"" + input + "\" (" + position + ") node \"" + node + "\": " + exception.getMessage())
                    .build();
        }

        @ApiStatus.Internal
        public static @NotNull CommandResult<String> inputError(@Nullable String input, @NotNull String node, int position, @NotNull InvalidInputException exception) {
            return CommandResult.<String>builder()
                    .type(CommandResult.Type.UNPROCESSABLE_CONTENT)
                    .resultCode(CommandErrorCodes.UNPROCESSABLE_CONTENT_INVALID_INPUT)
                    .message("Input \"" + input + "\" (" + position + ") not accepted by " + node + ": " + exception.getMessage())
                    .build();
        }

        @ApiStatus.Internal
        public static @NotNull CommandResult<String> nodeNotFound(@NotNull String input, int position) {
            return CommandResult.<String>builder()
                    .type(CommandResult.Type.NOT_FOUND)
                    .resultCode(CommandErrorCodes.NOT_FOUND_NODE_NOT_FOUND)
                    .message("Could not find a node for \"" + input + "\" (" + position + ")")
                    .build();
        }

        @ApiStatus.Internal
        public static @NotNull CommandResult<String> noCommandExecutor(@NotNull String node, int position) {
            return CommandResult.<String>builder()
                    .type(CommandResult.Type.NOT_IMPLEMENTED)
                    .resultCode(CommandErrorCodes.NOT_IMPLEMENTED_NO_COMMAND_EXECUTOR)
                    .message("The node \"" + node + "\" (" + position + ") does not have a command executor")
                    .build();
        }

        public static @NotNull CommandResult<String> noPermission(@Nullable String permission) {
            return CommandResult.<String>builder()
                    .type(CommandResult.Type.FORBIDDEN)
                    .resultCode(CommandErrorCodes.FORBIDDEN_NO_PERMISSION)
                    .message("Permission required: " + permission)
                    .build();
        }
    }
}