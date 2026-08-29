package com.wizardlybump17.wlib.command.result;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class CommandResult<T> {

    private final @Nullable T data;
    private final @NotNull Type type;
    private final @NotNull String resultCode;

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

    public static <T> @NotNull CommandResult<T> successful() {
        return new CommandResult<>(null, Type.SUCCESS, CommandErrorCodes.SUCCESS);
    }

    public static <T> @NotNull CommandResult<T> successful(@Nullable T data) {
        return new CommandResult<>(data, Type.SUCCESS, CommandErrorCodes.SUCCESS);
    }

    public static <T> @NotNull CommandResult<T> noContent() {
        return new CommandResult<>(null, Type.NO_CONTENT, CommandErrorCodes.NO_CONTENT);
    }

    public static <T> @NotNull CommandResult<T> noContent(@Nullable T data) {
        return new CommandResult<>(data, Type.NO_CONTENT, CommandErrorCodes.NO_CONTENT);
    }

    //ERROR

    public static <T> @NotNull CommandResult<T> badRequest() {
        return new CommandResult<>(null, Type.BAD_REQUEST, CommandErrorCodes.BAD_REQUEST_GENERIC);
    }

    public static <T> @NotNull CommandResult<T> badRequest(@NotNull String resultCode) {
        return new CommandResult<>(null, Type.BAD_REQUEST, resultCode);
    }

    public static <T> @NotNull CommandResult<T> conflict() {
        return new CommandResult<>(null, Type.CONFLICT, CommandErrorCodes.CONFLICT_GENERIC);
    }

    public static <T> @NotNull CommandResult<T> conflict(@NotNull String resultCode) {
        return new CommandResult<>(null, Type.CONFLICT, resultCode);
    }

    public static <T> @NotNull CommandResult<T> forbidden() {
        return new CommandResult<>(null, Type.FORBIDDEN, CommandErrorCodes.FORBIDDEN_GENERIC);
    }

    public static <T> @NotNull CommandResult<T> forbidden(@NotNull String resultCode) {
        return new CommandResult<>(null, Type.FORBIDDEN, resultCode);
    }

    public static <T> @NotNull CommandResult<T> genericError() {
        return new CommandResult<>(null, Type.GENERIC_ERROR, CommandErrorCodes.GENERIC_ERROR_GENERIC);
    }

    public static <T> @NotNull CommandResult<T> genericError(@NotNull String resultCode) {
        return new CommandResult<>(null, Type.GENERIC_ERROR, resultCode);
    }

    public static <T> @NotNull CommandResult<T> invalidSender() {
        return new CommandResult<>(null, Type.INVALID_SENDER, CommandErrorCodes.INVALID_SENDER_GENERIC);
    }

    public static <T> @NotNull CommandResult<T> invalidSender(@NotNull String resultCode) {
        return new CommandResult<>(null, Type.INVALID_SENDER, resultCode);
    }

    public static <T> @NotNull CommandResult<T> notFound() {
        return new CommandResult<>(null, Type.NOT_FOUND, CommandErrorCodes.NOT_FOUND_GENERIC);
    }

    public static <T> @NotNull CommandResult<T> notFound(@NotNull String resultCode) {
        return new CommandResult<>(null, Type.NOT_FOUND, resultCode);
    }

    public static <T> @NotNull CommandResult<T> unauthorized() {
        return new CommandResult<>(null, Type.UNAUTHORIZED, CommandErrorCodes.UNAUTHORIZED_GENERIC);
    }

    public static <T> @NotNull CommandResult<T> unauthorized(@NotNull String resultCode) {
        return new CommandResult<>(null, Type.UNAUTHORIZED, resultCode);
    }

    public static <T> @NotNull CommandResult<T> unprocessableContent() {
        return new CommandResult<>(null, Type.UNPROCESSABLE_CONTENT, CommandErrorCodes.UNPROCESSABLE_CONTENT_GENERIC);
    }

    public static <T> @NotNull CommandResult<T> unprocessableContent(@NotNull String resultCode) {
        return new CommandResult<>(null, Type.UNPROCESSABLE_CONTENT, resultCode);
    }

    public static <T> @NotNull CommandResult<T> notImplemented() {
        return new CommandResult<>(null, Type.NOT_IMPLEMENTED, CommandErrorCodes.NOT_IMPLEMENTED_GENERIC);
    }

    public static <T> @NotNull CommandResult<T> notImplemented(@NotNull String resultCode) {
        return new CommandResult<>(null, Type.NOT_IMPLEMENTED, resultCode);
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
}
