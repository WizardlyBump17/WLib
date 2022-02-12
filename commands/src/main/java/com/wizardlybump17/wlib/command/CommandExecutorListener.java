package com.wizardlybump17.wlib.command;

public interface CommandExecutorListener {

    boolean shouldExecute(CommandSender<?> sender, RegisteredCommand command);
}
