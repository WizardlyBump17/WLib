package com.wizardlybump17.wlib.command.holder;

public interface Command {

    void setExecutor(CommandExecutor executor);
    CommandExecutor getExecutor();
}
