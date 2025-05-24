package com.wizardlybump17.wlib.command.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SimpleCommandData extends CommandData {

    private final @NotNull Supplier<String> execution;
    private final @NotNull Supplier<String> permission;
    private final @NotNull Supplier<String> permissionMessage;
    private final @NotNull Supplier<Integer> priority;
    private final @NotNull Supplier<String> description;
    private final @NotNull Supplier<String> invalidSenderMessage;
    private final @NotNull Supplier<Class<?>> senderType;

    public SimpleCommandData(@NotNull Supplier<String> execution, @NotNull Supplier<String> permission, @NotNull Supplier<String> permissionMessage, @NotNull Supplier<Integer> priority, @NotNull Supplier<String> description, @NotNull Supplier<String> invalidSenderMessage, @NotNull Supplier<Class<?>> senderType) {
        this.execution = execution;
        this.permission = permission;
        this.permissionMessage = permissionMessage;
        this.priority = priority;
        this.description = description;
        this.invalidSenderMessage = invalidSenderMessage;
        this.senderType = senderType;
    }

    public SimpleCommandData(@NotNull String execution, @Nullable String permission, @Nullable String permissionMessage, int priority, @Nullable String description, @Nullable String invalidSenderMessage, @NotNull Class<?> senderType) {
        this(
                () -> execution,
                () -> permission,
                () -> permissionMessage,
                () -> priority,
                () -> description,
                () -> invalidSenderMessage,
                () -> senderType
        );
    }

    @Override
    public @NotNull String getExecution() {
        return execution.get();
    }

    @Override
    public @Nullable String getPermission() {
        return permission.get();
    }

    @Override
    public @Nullable String getPermissionMessage() {
        return permissionMessage.get();
    }

    @Override
    public int getPriority() {
        return priority.get();
    }

    @Override
    public @Nullable String getDescription() {
        return description.get();
    }

    @Override
    public @Nullable String getInvalidSenderMessage() {
        return invalidSenderMessage.get();
    }

    @Override
    public @NotNull Class<?> getSenderType() {
        return senderType.get();
    }

    public static class Builder {

        Builder() {
        }

        private @Nullable Supplier<String> execution;
        private @Nullable Supplier<String> permission;
        private @Nullable Supplier<String> permissionMessage;
        private @Nullable Supplier<Integer> priority;
        private @Nullable Supplier<String> description;
        private @Nullable Supplier<String> invalidSenderMessage;
        private @Nullable Supplier<Class<?>> senderType;

        public @Nullable String execution() {
            return execution == null ? null : execution.get();
        }

        public @NotNull Builder execution(@Nullable String execution) {
            this.execution = () -> execution;
            return this;
        }

        public @NotNull Builder execution(@Nullable Supplier<String> execution) {
            this.execution = execution;
            return this;
        }

        public @Nullable String permission() {
            return permission == null ? null : permission.get();
        }

        public @NotNull Builder permission(@Nullable String permission) {
            this.permission = () -> permission;
            return this;
        }

        public @NotNull Builder permission(@Nullable Supplier<String> permission) {
            this.permission = permission;
            return this;
        }

        public @Nullable String permissionMessage() {
            return permissionMessage == null ? null : permissionMessage.get();
        }

        public @NotNull Builder permissionMessage(@Nullable String permissionMessage) {
            this.permissionMessage = () -> permissionMessage;
            return this;
        }

        public @NotNull Builder permissionMessage(@Nullable Supplier<String> permissionMessage) {
            this.permissionMessage = permissionMessage;
            return this;
        }

        public @Nullable Integer priority() {
            return priority == null ? null : priority.get();
        }

        public @NotNull Builder priority(@Nullable Integer priority) {
            this.priority = () -> priority;
            return this;
        }

        public @NotNull Builder priority(@Nullable Supplier<Integer> priority) {
            this.priority = priority;
            return this;
        }

        public @Nullable String description() {
            return description == null ? null : description.get();
        }

        public @NotNull Builder description(@Nullable String description) {
            this.description = () -> description;
            return this;
        }

        public @NotNull Builder description(@Nullable Supplier<String> description) {
            this.description = description;
            return this;
        }

        public @Nullable String invalidSenderMessage() {
            return invalidSenderMessage == null ? null : invalidSenderMessage.get();
        }

        public @NotNull Builder invalidSenderMessage(@Nullable String invalidSenderMessage) {
            this.invalidSenderMessage = () -> invalidSenderMessage;
            return this;
        }

        public @NotNull Builder invalidSenderMessage(@Nullable Supplier<String> invalidSenderMessage) {
            this.invalidSenderMessage = invalidSenderMessage;
            return this;
        }

        public @Nullable Class<?> senderType() {
            return senderType == null ? null : senderType.get();
        }

        public @NotNull Builder senderType(@Nullable Class<?> senderType) {
            this.senderType = () -> senderType;
            return this;
        }

        public @NotNull Builder senderType(@Nullable Supplier<Class<?>> senderType) {
            this.senderType = senderType;
            return this;
        }

        public @NotNull CommandData build() {
            String execution = execution();
            if (execution == null)
                throw new NullPointerException("The execution can not be null");

            String permission = permission();
            String permissionMessage = permissionMessage();

            Integer priority = priority();
            if (priority == null)
                priority = execution.split(" ").length;

            String description = description();
            String invalidSenderMessage = invalidSenderMessage();

            Class<?> senderType = senderType();
            if (senderType == null)
                senderType = Object.class;

            return new SimpleCommandData(
                    execution,
                    permission,
                    permissionMessage,
                    priority,
                    description,
                    invalidSenderMessage,
                    senderType
            );
        }
    }
}
