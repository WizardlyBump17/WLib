package com.wizardlybump17.wlib.command.holder;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

/**
 * Represents something that can hold commands
 * @param <H> The holder type
 */
public interface CommandHolder<H> {

    @Nullable Command getCommand(String name);

    @NonNull H getHandle();

    @NonNull
    Logger getLogger();
}
