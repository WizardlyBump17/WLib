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
        execute(getSender(sender), command.getName(), args);
        return false;
    }

    @Override
    public void execute(com.wizardlybump17.wlib.command.CommandSender<?> sender, String commandName, String[] args) {
        String commandExecution = commandName + " " + String.join(" ", args);
        manager.execute(sender, commandExecution);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }

    private com.wizardlybump17.wlib.command.CommandSender<?> getSender(CommandSender original) {
        if (original instanceof Player player)
            return new PlayerSender(player);
        if (original instanceof ConsoleCommandSender consoleSender)
            return new ConsoleSender(consoleSender);
        if (original instanceof BlockCommandSender blockSender)
            return new com.wizardlybump17.wlib.command.sender.BlockCommandSender(blockSender);
        return new GenericSender(original);
    }
}
