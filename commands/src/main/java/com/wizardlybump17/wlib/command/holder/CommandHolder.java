package com.wizardlybump17.wlib.command.holder;

import com.wizardlybump17.wlib.command.CommandManager;
import com.wizardlybump17.wlib.command.RegisteredCommand;

/**
 * Represents something that can holds commands
 * @param <H> The holder type
 */
public interface CommandHolder<H> {

    Command getCommand(String name);
    H getHandle();
    void onCommandCreate(CommandManager manager, RegisteredCommand command);
    void onCommandDelete(CommandManager manager, RegisteredCommand command);
}
