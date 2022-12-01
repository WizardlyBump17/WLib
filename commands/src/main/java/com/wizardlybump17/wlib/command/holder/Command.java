package com.wizardlybump17.wlib.command.holder;

import com.wizardlybump17.wlib.command.CommandManager;

public interface Command {

    void setExecutor(CommandExecutor executor);

    CommandExecutor getExecutor();

    /**
     * <p>Used by the {@link com.wizardlybump17.wlib.command.CommandManager} when creating a new command.</p>
     * <p>This is used to set the default command executor.</p>
     * @param manager the command manager
     * @param name the command name
     * @return the default command executor
     */
    CommandExecutor getDefaultExecutor(CommandManager manager, String name);
}
