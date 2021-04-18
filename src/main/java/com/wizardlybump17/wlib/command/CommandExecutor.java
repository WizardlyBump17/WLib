package com.wizardlybump17.wlib.command;

import org.bukkit.command.CommandSender;

public interface CommandExecutor {

    void execute(CommandSender sender, String[] args, Object... params);
}
