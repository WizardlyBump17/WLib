package com.wizardlybump17.wlib.command;

import com.wizardlybump17.wlib.command.sender.ConsoleSender;
import com.wizardlybump17.wlib.command.sender.GenericSender;
import com.wizardlybump17.wlib.command.sender.PlayerSender;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@RequiredArgsConstructor
public class BukkitCommandExecutor implements TabExecutor, com.wizardlybump17.wlib.command.holder.CommandExecutor {

    private final CommandManager manager;

    @SneakyThrows
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        com.wizardlybump17.wlib.command.CommandSender<?> commandSender;
        if (sender instanceof Player player)
            commandSender = new PlayerSender(player);
        else if (sender instanceof ConsoleCommandSender consoleSender)
            commandSender = new ConsoleSender(consoleSender);
        else if (sender instanceof BlockCommandSender blockSender)
            commandSender = new com.wizardlybump17.wlib.command.sender.BlockCommandSender(blockSender);
        else
            commandSender = new GenericSender(sender);

        execute(commandSender, command.getName(), args);
        return false;
    }

    @Override
    public void execute(com.wizardlybump17.wlib.command.CommandSender<?> sender, String commandName, String[] args) {
        String commandExecution = commandName + " " + String.join(" ", args);
        manager.execute(sender, commandExecution);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
