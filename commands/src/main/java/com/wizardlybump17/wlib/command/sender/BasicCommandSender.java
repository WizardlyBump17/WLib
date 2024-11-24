package com.wizardlybump17.wlib.command.sender;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BasicCommandSender<S> implements CommandSender<S> {

    private final @NotNull S handle;
    private final @NotNull String name;
    private final @NotNull UUID id;
    private final @NotNull Consumer<String> messageConsumer;
    private final @NotNull Predicate<String> permissionTest;

    public BasicCommandSender(@NotNull S handle, @NotNull String name, @NotNull UUID id, @NotNull Consumer<String> messageConsumer, @NotNull Predicate<String> permissionTest) {
        this.handle = handle;
        this.name = name;
        this.id = id;
        this.messageConsumer = messageConsumer;
        this.permissionTest = permissionTest;
    }

    @Override
    public @NotNull S getHandle() {
        return handle;
    }

    @Override
    public void sendMessage(String message) {
        messageConsumer.accept(message);
    }

    @Override
    public void sendMessage(String... messages) {
        sendMessage(String.join("\n", messages));
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public boolean hasPermission(String permission) {
        return permissionTest.test(permission);
    }

    @Override
    public boolean hasId(@NotNull UUID id) {
        return id.equals(this.id);
    }

    public @NotNull UUID getId() {
        return id;
    }

    public @NotNull Consumer<String> getMessageConsumer() {
        return messageConsumer;
    }

    public @NotNull Predicate<String> getPermissionTest() {
        return permissionTest;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        BasicCommandSender<?> that = (BasicCommandSender<?>) o;
        return Objects.equals(handle, that.handle) && Objects.equals(name, that.name) && Objects.equals(id, that.id)
                && Objects.equals(messageConsumer, that.messageConsumer) && Objects.equals(permissionTest, that.permissionTest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(handle, name, id, messageConsumer, permissionTest);
    }

    @Override
    public String toString() {
        return "BasicCommandSender{" +
                "handle=" + handle +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", messageConsumer=" + messageConsumer +
                ", permissionTest=" + permissionTest +
                '}';
    }

    public static <S> Builder<S> builder() {
        return new Builder<>();
    }

    public static class Builder<S> {

        private @Nullable S handle;
        private @Nullable String name;
        private @Nullable UUID id;
        private @Nullable Consumer<String> messageConsumer;
        private @Nullable Predicate<String> permissionTest;

        protected Builder() {
        }

        public @Nullable S handle() {
            return handle;
        }

        public @NotNull Builder<S> handle(@Nullable S handle) {
            this.handle = handle;
            return this;
        }

        public @Nullable String name() {
            return name;
        }

        public @NotNull Builder<S> name(@Nullable String name) {
            this.name = name;
            return this;
        }

        public @Nullable UUID id() {
            return id;
        }

        public @NotNull Builder<S> id(@Nullable UUID id) {
            this.id = id;
            return this;
        }

        public @Nullable Consumer<String> messageConsumer() {
            return messageConsumer;
        }

        public @NotNull Builder<S> messageConsumer(@Nullable Consumer<String> messageConsumer) {
            this.messageConsumer = messageConsumer;
            return this;
        }

        public @Nullable Predicate<String> permissionTest() {
            return permissionTest;
        }

        public @NotNull Builder<S> permissionTest(@Nullable Predicate<String> permissionTest) {
            this.permissionTest = permissionTest;
            return this;
        }

        public @NotNull BasicCommandSender<S> build() {
            return new BasicCommandSender<>(
                    Objects.requireNonNull(handle, "the 'handler' can not be null"),
                    Objects.requireNonNull(name, "the 'name' can not be null"),
                    Objects.requireNonNull(id, "the 'id' can not be null"),
                    messageConsumer == null ? System.out::println : messageConsumer,
                    permissionTest == null ? permission -> true : permissionTest
            );
        }
    }
}
