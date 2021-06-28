package com.wizardlybump17.wlib.command;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class BukkitCommandHandler implements org.bukkit.command.CommandExecutor {

    private final CommandManager manager;

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        List<String> argsList = new ArrayList<>();
        argsList.add(command.getName());
        argsList.addAll(Arrays.asList(args));
        manager.execute(sender, command.getName(), argsList.toArray(new String[]{}));
        return true;
    }
}
