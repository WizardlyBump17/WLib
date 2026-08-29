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

    private static final @NotNull CommandResult<?> SUCCESSFUL = new CommandResult<>(null, Type.SUCCESS, CommandErrorCodes.SUCCESS);
    private static final @NotNull CommandResult<?> NO_CONTENT_RESULT = new CommandResult<>(null, Type.NO_CONTENT, CommandErrorCodes.NO_CONTENT);
    private static final @NotNull CommandResult<?> BAD_REQUEST_RESULT = new CommandResult<>(null, Type.BAD_REQUEST, CommandErrorCodes.BAD_REQUEST_GENERIC);
    private static final @NotNull CommandResult<?> CONFLICT_RESULT = new CommandResult<>(null, Type.CONFLICT, CommandErrorCodes.CONFLICT_GENERIC);
    private static final @NotNull CommandResult<?> FORBIDDEN_RESULT = new CommandResult<>(null, Type.FORBIDDEN, CommandErrorCodes.FORBIDDEN_GENERIC);
    private static final @NotNull CommandResult<?> GENERIC_ERROR_RESULT = new CommandResult<>(null, Type.GENERIC_ERROR, CommandErrorCodes.GENERIC_ERROR_GENERIC);
    private static final @NotNull CommandResult<?> INVALID_SENDER_RESULT = new CommandResult<>(null, Type.INVALID_SENDER, CommandErrorCodes.INVALID_SENDER_GENERIC);
    private static final @NotNull CommandResult<?> NOT_FOUND_RESULT = new CommandResult<>(null, Type.NOT_FOUND, CommandErrorCodes.NOT_FOUND_GENERIC);
    private static final @NotNull CommandResult<?> UNAUTHORIZED_RESULT = new CommandResult<>(null, Type.UNAUTHORIZED, CommandErrorCodes.UNAUTHORIZED_GENERIC);
    private static final @NotNull CommandResult<?> UNPROCESSABLE_CONTENT_RESULT = new CommandResult<>(null, Type.UNPROCESSABLE_CONTENT, CommandErrorCodes.UNPROCESSABLE_CONTENT_GENERIC);
    private static final @NotNull CommandResult<?> NOT_IMPLEMENTED_RESULT = new CommandResult<>(null, Type.NOT_IMPLEMENTED, CommandErrorCodes.NOT_IMPLEMENTED_GENERIC);

    private CommandResult(@Nullable T data, @NotNull Type type, @NotNull String resultCode) {
        this.data = data;
        this.type = type;
        this.resultCode = resultCode;
    }

    public boolean success() {
        return type.isSuccess();
    }

    public @Nullable T data() {
        return data;
    }

    public @NotNull Type type() {
        return type;
    }

    public @NotNull String resultCode() {
        return resultCode;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (other == null || getClass() != other.getClass())
            return false;
        CommandResult<?> that = (CommandResult<?>) other;
        return Objects.equals(data, that.data) && Objects.equals(type, that.type) && Objects.equals(resultCode, that.resultCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, type, resultCode);
    }

    @Override
    public String toString() {
        return "CommandResult{" +
                "data=" + data +
                ", type='" + type + '\'' +
                ", resultCode=" + resultCode +
                '}';
    }

    //SUCCESS

    @SuppressWarnings("unchecked")
    public static <T> @NotNull CommandResult<T> successful() {
        return (CommandResult<T>) SUCCESSFUL;
    }

    public static <T> @NotNull CommandResult<T> successful(@Nullable T data) {
        return new CommandResult<>(data, Type.SUCCESS, CommandErrorCodes.SUCCESS);
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull CommandResult<T> noContent() {
        return (CommandResult<T>) NO_CONTENT_RESULT;
    }

    public static <T> @NotNull CommandResult<T> noContent(@Nullable T data) {
        return new CommandResult<>(data, Type.NO_CONTENT, CommandErrorCodes.NO_CONTENT);
    }

    //ERROR

    @SuppressWarnings("unchecked")
    public static <T> @NotNull CommandResult<T> badRequest() {
        return (CommandResult<T>) BAD_REQUEST_RESULT;
    }

    public static <T> @NotNull CommandResult<T> badRequest(@NotNull String resultCode) {
        return new CommandResult<>(null, Type.BAD_REQUEST, resultCode);
    }

    public static <T> @NotNull CommandResult<T> badRequest(@NotNull String resultCode, @Nullable T data) {
        return new CommandResult<>(data, Type.BAD_REQUEST, resultCode);
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull CommandResult<T> conflict() {
        return (CommandResult<T>) CONFLICT_RESULT;
    }

    public static <T> @NotNull CommandResult<T> conflict(@NotNull String resultCode) {
        return new CommandResult<>(null, Type.CONFLICT, resultCode);
    }

    public static <T> @NotNull CommandResult<T> conflict(@NotNull String resultCode, @Nullable T data) {
        return new CommandResult<>(data, Type.CONFLICT, resultCode);
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull CommandResult<T> forbidden() {
        return (CommandResult<T>) FORBIDDEN_RESULT;
    }

    public static <T> @NotNull CommandResult<T> forbidden(@NotNull String resultCode) {
        return new CommandResult<>(null, Type.FORBIDDEN, resultCode);
    }

    public static <T> @NotNull CommandResult<T> forbidden(@NotNull String resultCode, @Nullable T data) {
        return new CommandResult<>(data, Type.FORBIDDEN, resultCode);
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull CommandResult<T> genericError() {
        return (CommandResult<T>) GENERIC_ERROR_RESULT;
    }

    public static <T> @NotNull CommandResult<T> genericError(@NotNull String resultCode) {
        return new CommandResult<>(null, Type.GENERIC_ERROR, resultCode);
    }

    public static <T> @NotNull CommandResult<T> genericError(@NotNull String resultCode, @Nullable T data) {
        return new CommandResult<>(data, Type.GENERIC_ERROR, resultCode);
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull CommandResult<T> invalidSender() {
        return (CommandResult<T>) INVALID_SENDER_RESULT;
    }

    public static <T> @NotNull CommandResult<T> invalidSender(@NotNull String resultCode) {
        return new CommandResult<>(null, Type.INVALID_SENDER, resultCode);
    }

    public static <T> @NotNull CommandResult<T> invalidSender(@NotNull String resultCode, @Nullable T data) {
        return new CommandResult<>(data, Type.INVALID_SENDER, resultCode);
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull CommandResult<T> notFound() {
        return (CommandResult<T>) NOT_FOUND_RESULT;
    }

    public static <T> @NotNull CommandResult<T> notFound(@NotNull String resultCode) {
        return new CommandResult<>(null, Type.NOT_FOUND, resultCode);
    }

    public static <T> @NotNull CommandResult<T> notFound(@NotNull String resultCode, @Nullable T data) {
        return new CommandResult<>(data, Type.NOT_FOUND, resultCode);
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull CommandResult<T> unauthorized() {
        return (CommandResult<T>) UNAUTHORIZED_RESULT;
    }

    public static <T> @NotNull CommandResult<T> unauthorized(@NotNull String resultCode) {
        return new CommandResult<>(null, Type.UNAUTHORIZED, resultCode);
    }

    public static <T> @NotNull CommandResult<T> unauthorized(@NotNull String resultCode, @Nullable T data) {
        return new CommandResult<>(data, Type.UNAUTHORIZED, resultCode);
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull CommandResult<T> unprocessableContent() {
        return (CommandResult<T>) UNPROCESSABLE_CONTENT_RESULT;
    }

    public static <T> @NotNull CommandResult<T> unprocessableContent(@NotNull String resultCode) {
        return new CommandResult<>(null, Type.UNPROCESSABLE_CONTENT, resultCode);
    }

    public static <T> @NotNull CommandResult<T> unprocessableContent(@NotNull String resultCode, @Nullable T data) {
        return new CommandResult<>(data, Type.UNPROCESSABLE_CONTENT, resultCode);
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull CommandResult<T> notImplemented() {
        return (CommandResult<T>) NOT_IMPLEMENTED_RESULT;
    }

    public static <T> @NotNull CommandResult<T> notImplemented(@NotNull String resultCode) {
        return new CommandResult<>(null, Type.NOT_IMPLEMENTED, resultCode);
    }

    public static <T> @NotNull CommandResult<T> notImplemented(@NotNull String resultCode, @Nullable T data) {
        return new CommandResult<>(data, Type.NOT_IMPLEMENTED, resultCode);
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

        public @NotNull CommandResult<T> build() {
            return new CommandResult<>(
                    data,
                    Objects.requireNonNull(type, "The type can not be null"),
                    Objects.requireNonNull(resultCode, "The resultCode can not be null")
            );
        }
    }

    public static final class Errors {

        private static final @NotNull CommandResult<String> EMPTY_INPUT = CommandResult.<String>builder()
                .type(Type.BAD_REQUEST)
                .resultCode(CommandErrorCodes.BAD_REQUEST_EMPTY_INPUT)
                .data("The input can not be empty")
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
                    .data("Command \"" + command + "\" not found")
                    .build();
        }

        @ApiStatus.Internal
        public static @NotNull CommandResult<String> parseError(@NotNull String input, @NotNull String node, int position, @NotNull InputParsingException exception) {
            return CommandResult.<String>builder()
                    .type(CommandResult.Type.BAD_REQUEST)
                    .resultCode(CommandErrorCodes.BAD_REQUEST_PARSE_ERROR)
                    .data("Error while parsing the input \"" + input + "\" (" + position + ") node \"" + node + "\": " + exception.getMessage())
                    .build();
        }

        @ApiStatus.Internal
        public static @NotNull CommandResult<String> inputError(@Nullable String input, @NotNull String node, int position, @NotNull InvalidInputException exception) {
            return CommandResult.<String>builder()
                    .type(CommandResult.Type.UNPROCESSABLE_CONTENT)
                    .resultCode(CommandErrorCodes.UNPROCESSABLE_CONTENT_INVALID_INPUT)
                    .data("Input \"" + input + "\" (" + position + ") not accepted by " + node + ": " + exception.getMessage())
                    .build();
        }

        @ApiStatus.Internal
        public static @NotNull CommandResult<String> nodeNotFound(@NotNull String input, int position) {
            return CommandResult.<String>builder()
                    .type(CommandResult.Type.NOT_FOUND)
                    .resultCode(CommandErrorCodes.NOT_FOUND_NODE_NOT_FOUND)
                    .data("Could not find a node for \"" + input + "\" (" + position + ")")
                    .build();
        }

        @ApiStatus.Internal
        public static @NotNull CommandResult<String> noCommandExecutor(@NotNull String node, int position) {
            return CommandResult.<String>builder()
                    .type(CommandResult.Type.NOT_IMPLEMENTED)
                    .resultCode(CommandErrorCodes.NOT_IMPLEMENTED_NO_COMMAND_EXECUTOR)
                    .data("The node \"" + node + "\" (" + position + ") does not have a command executor")
                    .build();
        }

        public static @NotNull CommandResult<String> noPermission(@Nullable String permission) {
            return CommandResult.<String>builder()
                    .type(CommandResult.Type.FORBIDDEN)
                    .resultCode(CommandErrorCodes.FORBIDDEN_NO_PERMISSION)
                    .data("Permission required: " + permission)
                    .build();
        }
    }
}
