package com.wizardlybump17.wlib.command.sender;

import org.jetbrains.annotations.NotNull;

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
}
