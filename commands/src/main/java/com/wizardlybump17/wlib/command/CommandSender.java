package com.wizardlybump17.wlib.command;

import org.jetbrains.annotations.Nullable;

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

    String getName();

    boolean hasPermission(String permission);

    /**
     * Used to convert this CommandSender to a generic sender, a sender that can be anything
     *
     * @return the generic sender
     */
    @Nullable
    default CommandSender<?> toGeneric() {
        return null;
    }
}
