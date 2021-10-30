package com.wizardlybump17.wlib.command;

import com.wizardlybump17.wlib.command.holder.CommandExecutor;

import java.util.Collection;

/**
 * Used by WLib at Bukkit for intercepting command creation and deleting for to register/unregister the command.
 * Can be used outside the Bukkit context
 */
public interface CommandManagerListener {

    /**
     * @return the current registered CommandExecutors
     */
    Collection<CommandExecutor> getExecutors();

    /**
     * Called when a command is deleted from the CommandManager
     * @param manager the manager were the command was deleted
     * @param command the command that got deleted
     */
    void onCommandDelete(CommandManager manager, RegisteredCommand command);

    /**
     * Called when a command is created in the CommandManager
     * @param manager the manager that received the command
     * @param command the command that was created
     */
    void onCommandCreate(CommandManager manager, RegisteredCommand command);
}
