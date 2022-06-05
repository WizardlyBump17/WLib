package com.wizardlybump17.wlib.bungee.command;

import com.wizardlybump17.wlib.command.CommandManager;
import com.wizardlybump17.wlib.command.holder.Command;
import com.wizardlybump17.wlib.command.holder.CommandExecutor;

public class BungeeCommand implements Command {

    private CommandExecutor executor;

    @Override
    public void setExecutor(CommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public CommandExecutor getExecutor() {
        return executor;
    }

    @Override
    public void setDefaultExecutor(CommandManager manager, String name) {
        setExecutor(new BungeeCommandExecutor(manager, name));
    }
}
