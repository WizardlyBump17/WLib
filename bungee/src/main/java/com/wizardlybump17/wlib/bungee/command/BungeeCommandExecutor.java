package com.wizardlybump17.wlib.bungee.command;

import com.wizardlybump17.wlib.bungee.command.sender.BungeeCommandSender;
import com.wizardlybump17.wlib.command.CommandManager;
import com.wizardlybump17.wlib.command.exception.CommandException;
import com.wizardlybump17.wlib.command.holder.CommandExecutor;
import net.md_5.bungee.api.plugin.Command;

import java.util.logging.Level;

public class BungeeCommandExecutor extends Command implements CommandExecutor {

    private final CommandManager manager;

    public BungeeCommandExecutor(CommandManager manager, String name) {
        super(name);
        this.manager = manager;
    }

    @Override
    public void execute(com.wizardlybump17.wlib.command.sender.CommandSender<?> sender, String commandName, String[] args) throws CommandException {
        manager.execute(sender, commandName + " " + String.join(" ", args));
    }

    @Override
    public void execute(net.md_5.bungee.api.CommandSender sender, String[] args) {
        try {
            execute(new BungeeCommandSender(sender), getName(), args);
        } catch (CommandException e) {
            manager.getHolder().getLogger().log(Level.SEVERE, "Error while executing a command", e);
        }
    }
}
