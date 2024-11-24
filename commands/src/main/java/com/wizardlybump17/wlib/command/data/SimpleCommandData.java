package com.wizardlybump17.wlib.command.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SimpleCommandData extends CommandData {

    private final @NotNull String execution;
    private final @Nullable String permission;
    private final @Nullable String permissionMessage;
    private final int priority;
    private final @Nullable String description;
    private final @Nullable String invalidSenderMessage;
    private final @NotNull Class<?> senderType;

    public SimpleCommandData(@NotNull String execution, @Nullable String permission, @Nullable String permissionMessage, int priority, @Nullable String description, @Nullable String invalidSenderMessage, @NotNull Class<?> senderType) {
        this.execution = execution;
        this.permission = permission;
        this.permissionMessage = permissionMessage;
        this.priority = priority;
        this.description = description;
        this.invalidSenderMessage = invalidSenderMessage;
        this.senderType = senderType;
    }

    @Override
    public @NotNull String getExecution() {
        return execution;
    }

    @Override
    public @Nullable String getPermission() {
        return permission;
    }

    @Override
    public @Nullable String getPermissionMessage() {
        return permissionMessage;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public @Nullable String getDescription() {
        return description;
    }

    @Override
    public @Nullable String getInvalidSenderMessage() {
        return invalidSenderMessage;
    }

    @Override
    public @NotNull Class<?> getSenderType() {
        return senderType;
    }

    public static class Builder {

        Builder() {
        }

        private @Nullable String execution;
        private @Nullable String permission;
        private @Nullable String permissionMessage;
        private @Nullable Integer priority;
        private @Nullable String description;
        private @Nullable String invalidSenderMessage;
        private @Nullable Class<?> senderType;

        public @Nullable String execution() {
            return execution;
        }

        public @NotNull Builder execution(@Nullable String execution) {
            this.execution = execution;
            return this;
        }

        public @Nullable String permission() {
            return permission;
        }

        public @NotNull Builder permission(@Nullable String permission) {
            this.permission = permission;
            return this;
        }

        public @Nullable String permissionMessage() {
            return permissionMessage;
        }

        public @NotNull Builder permissionMessage(@Nullable String permissionMessage) {
            this.permissionMessage = permissionMessage;
            return this;
        }

        public @Nullable Integer priority() {
            return priority;
        }

        public @NotNull Builder priority(@Nullable Integer priority) {
            this.priority = priority;
            return this;
        }

        public @Nullable String description() {
            return description;
        }

        public @NotNull Builder description(@Nullable String description) {
            this.description = description;
            return this;
        }

        public @Nullable String invalidSenderMessage() {
            return invalidSenderMessage;
        }

        public @NotNull Builder invalidSenderMessage(@Nullable String invalidSenderMessage) {
            this.invalidSenderMessage = invalidSenderMessage;
            return this;
        }

        public @Nullable Class<?> senderType() {
            return senderType;
        }

        public @NotNull Builder senderType(@Nullable Class<?> senderType) {
            this.senderType = senderType;
            return this;
        }

        public @NotNull CommandData build() {
            return new SimpleCommandData(
                    Objects.requireNonNull(execution, "The execution can not be null"),
                    permission,
                    permissionMessage,
                    priority == null ? execution.split(" ").length : priority,
                    description,
                    invalidSenderMessage,
                    senderType == null ? Object.class : senderType
            );
        }
    }
}
