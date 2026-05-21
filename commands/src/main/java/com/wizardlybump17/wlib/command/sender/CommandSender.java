package com.wizardlybump17.wlib.command.sender;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Represents a command sender, someone that can trigger commands.
 *
 * @param <S> The type of the sender
 */
public interface CommandSender<S> {

    /**
     * @return the sender handle, the real sender
     */
    S getHandle();

    void sendMessage(String message);

    void sendMessage(String... messages);

    void sendMessage(@Nullable Object message);

    void sendMessage(@Nullable Object @Nullable ... messages);

    String getName();

    boolean hasPermission(String permission);

    boolean hasId(@NotNull UUID id);
}
