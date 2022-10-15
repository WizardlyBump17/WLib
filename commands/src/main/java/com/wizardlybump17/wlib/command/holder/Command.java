package com.wizardlybump17.wlib.command.holder;

import com.wizardlybump17.wlib.command.CommandManager;

public interface Command {

    void setExecutor(CommandExecutor executor);

    CommandExecutor getExecutor();

    /**
     * <p>Used by the {@link com.wizardlybump17.wlib.command.CommandManager} when creating a new command.</p>
     * <p>This is used to set the default command executor.</p>
     * <p>The {@link #getExecutor()} should return the default command executor if the executor has not been set</p>
     * @param manager the {@link com.wizardlybump17.wlib.command.CommandManager} that is creating the command
     * @param name the command name
     */
    default void setDefaultExecutor(CommandManager manager, String name) {
    }
}
