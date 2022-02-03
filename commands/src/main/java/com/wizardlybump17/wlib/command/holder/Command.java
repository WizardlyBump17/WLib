package com.wizardlybump17.wlib.command.holder;

import com.wizardlybump17.wlib.command.CommandManager;

public interface Command {

    void setExecutor(CommandExecutor executor);

    CommandExecutor getExecutor();

    /**
     * Used by the {@link com.wizardlybump17.wlib.command.CommandManager} when creating a new command.
     * This is used to set the default command executor.
     * The {@link #getExecutor()} should return the default command executor if the executor has not been set
     * @param manager The {@link com.wizardlybump17.wlib.command.CommandManager} that is creating the command
     */
    default void setDefaultExecutor(CommandManager manager) {
    }
}
