package com.wizardlybump17.wlib.bungee.command;

import com.wizardlybump17.wlib.bungee.command.sender.GenericSender;
import com.wizardlybump17.wlib.bungee.command.sender.ProxiedPlayerSender;
import com.wizardlybump17.wlib.command.CommandManager;
import com.wizardlybump17.wlib.command.CommandSender;
import com.wizardlybump17.wlib.command.holder.CommandExecutor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BungeeCommandExecutor extends Command implements CommandExecutor {

    private final CommandManager manager;

    public BungeeCommandExecutor(CommandManager manager, String name) {
        super(name);
        this.manager = manager;
    }

    @Override
    public void execute(CommandSender<?> sender, String commandName, String[] args) {
        manager.execute(sender, commandName + " " + String.join(" ", args));
    }

    @Override
    public void execute(net.md_5.bungee.api.CommandSender sender, String[] args) {
        CommandSender<?> realSender;
        if (sender instanceof ProxiedPlayer)
            realSender = new ProxiedPlayerSender((ProxiedPlayer) sender);
        else
            realSender = new GenericSender(sender);
        execute(realSender, getName(), args);
    }
}
