package com.wizardlybump17.wlib.command.holder;

import com.wizardlybump17.wlib.command.args.reader.ArgsReaderException;
import com.wizardlybump17.wlib.command.exception.CommandException;
import com.wizardlybump17.wlib.command.sender.CommandSender;

/**
 * Interface for intercepting commands when they are called
 */
public interface CommandExecutor {

    void execute(CommandSender<?> sender, String commandName, String[] args) throws ArgsReaderException, CommandException;
}
